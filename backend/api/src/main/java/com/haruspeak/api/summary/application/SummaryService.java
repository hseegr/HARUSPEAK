package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentJpaRepository;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.ThumbnailRegenState;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.request.DailySummaryCreateRequest;
import com.haruspeak.api.summary.dto.response.DailySummaryCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final DailySummaryRepository dailySummaryRepository;
    private final ActiveDailyMomentJpaRepository activeDailyMomentJpaRepository;
    private final FastApiClient fastApiClient;
    private final RedisTemplate<String, Object> redisTemplate;

    // [API] AI 하루일기 요약 재생성
    @Transactional
    public DailySummaryCreateResponse regenerateDailySummary (Integer userId, Integer summaryId, String uri) {

        // dailySummary 불러오기 -> userId 로 user 것이 맞는지 확인
        DailySummary dailySummary = dailySummaryRepository.findById(summaryId)
                .orElseThrow(() -> new HaruspeakException(ErrorCode.DIARY_NOT_FOUND));
        if(dailySummary.getUserId() != userId) throw new HaruspeakException(ErrorCode.UNAUTHORIZED); // user 검증
        if(dailySummary.isDeleted()) throw new HaruspeakException(ErrorCode.DIARY_NOT_FOUND); // soft delete 여부 검증

        // 요약내용생성횟수가 3회 이상이면 더이상 요청 불가
        if(dailySummary.getContentGenerateCount() >= 3) throw new HaruspeakException(ErrorCode.SUMMARY_CONTENT_GENERATE_COUNT_LIMIT_EXCEEDED);

        // summaryId 에 해당하는 순간기록들 리스트로 모두 불러오기
        List<ActiveDailyMoment> moments = activeDailyMomentJpaRepository.findBySummaryId(summaryId);

        // 그 리스트 요소들 하나의 String 으로 이어붙이기
        String mergedMoments = moments.stream()
                .map(ActiveDailyMoment::getContent)
                .collect(Collectors.joining(""));

        DailySummaryCreateRequest dscr = new DailySummaryCreateRequest(mergedMoments);

        try {
            // ai 서버에 프론트 요청값 전달 후 반환 받기
            DailySummaryCreateResponse response = fastApiClient.getPrediction(uri, dscr, DailySummaryCreateResponse.class);
            // 성공 시 요약내용재생성횟수 1회 증가
            dailySummary.increaseContentGenerateCount();
            return response;
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.SUMMARY_CONTENT_REGENERATION_FAILED); // 필요에 따라 ErrorCode 조정
        }
    }

    // [API] AI 하루일기 썸네일 재생성
    @Transactional
    public void queueRegenerateDailyThumbnail (
            Integer summaryId,
            Integer userId
    ) {

        // RDB 에서 summaryId 에 해당하는 dailySummary 가져오기, 없으면 에러처리
        DailySummary dailySummary = dailySummaryRepository.findById(summaryId)
                .orElseThrow(() -> new HaruspeakException(ErrorCode.DIARY_NOT_FOUND));

        if(dailySummary.getUserId() != userId) throw new HaruspeakException(ErrorCode.UNAUTHORIZED); // user 검증
        if(dailySummary.isDeleted()) throw new HaruspeakException(ErrorCode.DIARY_NOT_FOUND); // soft delete 여부 검증

        // 재생성 요청 횟수가 3 이상일 경우, 횟수초과 에러
        if(dailySummary.getImageGenerateCount() >= 3) throw new HaruspeakException(ErrorCode.THUMBNAIL_REGEN_REQUEST_LIMIT_EXCEEDED);

        // "상태열"에 관련 요소들(키, 필드) 세팅
        String stateKey = "user:" + userId + ":image:regeneration"; // userId 로 만든 redis key 형식
        String redisField = String.valueOf(summaryId);

        // redis 에 key, summaryId 에 해당하는 데이터 찾기
        Object redisData = redisTemplate.opsForHash().get(stateKey, redisField);

        // redis 데이터가 이미 있으면, 대기열에 이미 있으니 재요청 안됨 -> 에러
        if(redisData != null) throw new HaruspeakException(ErrorCode.THUMBNAIL_REGEN_CONFLICT);

        // redisData 없으면, "상태열" 추가
        Map<String, Object> thumbnailData = new HashMap<>();
        thumbnailData.put("state", ThumbnailRegenState.QUEUED);
        thumbnailData.put("timestamp", LocalDateTime.now().toString());
        thumbnailData.put("retryCount", 1);
        redisTemplate.opsForHash().put(stateKey, redisField, thumbnailData);

        // redisData 없으면, "재생성 대기열" 추가
        String regenerateKey = "thumbnail:regeneration:queue";
        redisTemplate.opsForList().rightPush(regenerateKey, summaryId);
    }

}


