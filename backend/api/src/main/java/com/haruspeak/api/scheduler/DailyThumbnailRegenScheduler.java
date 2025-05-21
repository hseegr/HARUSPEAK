package com.haruspeak.api.scheduler;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.s3.S3Service;
import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.ThumbnailRegenState;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.request.DailyThumbnailCreateRequest;
import com.haruspeak.api.summary.dto.response.DailyThumbnailCreateResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyThumbnailRegenScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConnectionFactory redisConnectionFactory;
    private final DailySummaryRepository dailySummaryRepository;
    private final FastApiClient fastApiClient;
    private final S3Service s3Service;

    @Transactional
    @Scheduled(cron = "*/20 * * * * *")
    public void regenerateDailyThumbnail() {

//        System.out.println("Thumbnail Regen Scheduler Start !!");

        // 재생성 대기열 모두 꺼내 담은 후 모두 삭제
        String regenerateKey = "thumbnail:regeneration:queue"; // 키 설정
        List<Object> summaryIdsListForRegen = redisTemplate.opsForList()
                .range(regenerateKey, 0, -1); // 재생성 대기열(summaryIds) 다 꺼낸 List
        redisTemplate.delete("thumbnail:regeneration:queue"); // 재생성 대기열 redis 에서 모두 삭제

        // 재생성 대기열 순회하며 재생성 시도 시작
        for (Object summaryId : summaryIdsListForRegen) {

            // summaryId 를 Integer 로 역직렬화
            Integer deserializedSummaryId = (Integer) summaryId;

            // RDB 에서 일기 내용과 dailySummary 객체 가져와서
            DailySummary dailySummary = dailySummaryRepository.findById(deserializedSummaryId)
                    .orElseThrow(() -> new HaruspeakException(ErrorCode.DIARY_NOT_FOUND));

            // 필요 요소 빼내기
            Integer userId = dailySummary.getUserId(); // userId
            String content = dailySummary.getContent(); // 요약된 일기내용

            // 상태열 접근 위한 요소 세팅
            String stateKey = "user:" + userId + ":image:regeneration";
            String redisField = String.valueOf(summaryId);

            // Redis 에서 userId 와 summaryId 에 부합하는 데이터 가져와 "상태열" 상태, 재시도 횟수 변경
            Object redisData = redisTemplate.opsForHash().get(stateKey, redisField); // 가져오기
            Map<String, Object> thumbnailData = (Map<String, Object>) redisData; // 형변환
            if(thumbnailData == null) continue;
            Integer retryCount = (Integer) thumbnailData.get("retryCount"); // 재시도 횟수 가져오기

            // 재시도 횟수가 3 이상이면 상태 FAILED 처리 및 에러 던지기
            if( retryCount >= 3 ) {
                thumbnailData.put("state", ThumbnailRegenState.FAILED);
                redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData); // FAILED 상태 반영
                redisTemplate.opsForHash().delete(stateKey, redisField); // 상태열 필드 삭제

                throw new HaruspeakException(ErrorCode.THUMBNAIL_REGEN_REDIS_RETRY_COUNT_LIMIT_EXCEEDED); // 3회 초과시 에러
            }

            // 생성 중인 상태일 때 에러 던지기
            if (thumbnailData.get("state") == ThumbnailRegenState.GENERATING
                    || thumbnailData.get("state") == ThumbnailRegenState.RETRYING) {
                throw new HaruspeakException(ErrorCode.THUMBNAIL_REGENERATING_CONFLICT);
            }

            thumbnailData.put("state", ThumbnailRegenState.PENDING); // redis 에 상태변경 (대기중)
            redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData); // redis 에 변경사항 반영

            // ai 서버에 프론트 요청값 전달 후 반환 받기
            String uri = "/ai/daily-thumbnail-dalle";
            DailyThumbnailCreateRequest dtcReq = new DailyThumbnailCreateRequest(content); // 요청값 담은 DTO

            try {
                // 요청 직전 상태변경
                if(retryCount == 1) {
                    thumbnailData.put("state", ThumbnailRegenState.GENERATING);
                } else if (retryCount == 2) {
                    thumbnailData.put("state", ThumbnailRegenState.RETRYING);
                }
                thumbnailData.put("retryCount", retryCount+1); // 재생성 시도 1회 증가
                redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData); // redis 에 변경사항 반영

                DailyThumbnailCreateResponse dtcResp = fastApiClient.getPrediction(
                        uri,
                        dtcReq,
                        DailyThumbnailCreateResponse.class
                );

                // S3 업로드
                String thumbnailImageUrl = s3Service.uploadImagesAndGetUrls("data:image/png;base64," + dtcResp.base64());
                dailySummary.updateImageUrl(thumbnailImageUrl); // RDB 반영

                // "상태열" 삭제
                redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData); // redis 에 변경사항 반영
                redisTemplate.opsForHash().delete(stateKey, redisField); // 상태열 필드 삭제

                // 재생성 횟수+1
                dailySummary.increaseImageGenerateCount(); // imageGenerateCount++

            } catch (Exception e) {

                // 실패시 다시 "재생성 대기열" 추가
                redisTemplate.opsForList().rightPush(regenerateKey, summaryId);

                thumbnailData.put("state", ThumbnailRegenState.PENDING); // 실패시에 state 변경,
                redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData); // 변경 저장

                throw new RuntimeException("썸네일 재생성 시도 중 오류가 발생했습니다.", e);
            }
        }
    }
}
