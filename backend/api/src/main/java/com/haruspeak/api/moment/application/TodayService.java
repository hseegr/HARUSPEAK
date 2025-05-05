package com.haruspeak.api.moment.application;


import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.s3.S3Service;
import com.haruspeak.api.moment.dto.TodayMoment;
import com.haruspeak.api.moment.dto.request.MomentUpdateRequest;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import com.haruspeak.api.moment.dto.response.TodayMomentListResponse;
import com.haruspeak.api.today.domain.repository.TodayMomentRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayService {

    private final TodayMomentRedisRepository todayRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final S3Service s3Service;

    /**
     * 순간 일기 작성
     * @param request
     * @param userId
     */
    public void saveMoment(MomentWriteRequest request, Integer userId) {
        String date = LocalDate.now().toString();
        String key = "user:" + userId + ":moment:" + date;
        String now = LocalDateTime.now().toString();

        List<String> images = request.images().stream()
                .map(s3Service::uploadImagesAndGetUrls)
                .toList();

        Map<String, Object> momentData = new HashMap<>();
        momentData.put("momentTime", now);
        momentData.put("content", request.content());
        momentData.put("images", images);
        momentData.put("tags", List.of());

        try {
            redisTemplate.opsForHash().put(key, now, momentData);
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_SAVE_ERROR);
        }
    }

    /**
     * 순간 일기 수정
     * @param time
     * @param request
     * @param userId
     */
    public void updateMoment(String time, MomentUpdateRequest request, Integer userId){
        String date = LocalDate.now().toString();

        String key = "user:" + userId + ":moment:" + date;

        try {
            request.deletedImages().forEach(s3Service::deleteImages);

            List<String> images = request.images().stream()
                    .map(image -> image.startsWith("data:image")
                            ? s3Service.uploadImagesAndGetUrls(image)
                            : image)
                    .toList();

            Map<String, Object> existingMoment = (Map<String, Object>) redisTemplate.opsForHash().get(key, time);

            if (existingMoment == null) throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);

            existingMoment.put("momentTime", request.momentTime());
            existingMoment.put("content", request.content());
            existingMoment.put("images", images);
            existingMoment.put("tags", request.tags());

            redisTemplate.opsForHash().put(key, time, existingMoment);

        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_UPDATE_ERROR);
        }
    }

    /**
     * 오늘 순간 일기 삭제
     * @param time
     * @param userId
     */
    public void removeMoment(String time, Integer userId){
        String date = LocalDate.now().toString();
        String key = "user:" + userId + ":moment:" + date;

        try {
            Map<String, Object> moment = (Map<String, Object>) redisTemplate.opsForHash().get(key, time);

            if (moment == null) throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);

            ofNullable((List<String>) moment.get("images"))
                    .ifPresent(images -> images.forEach(s3Service::deleteImages));

            redisTemplate.opsForHash().delete(key, time);

        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_DELETE_ERROR);
        }
    }

    /**
     * 오늘의 순간 일기들 불러오기
     * @param userId
     * @return
     */
    public TodayMomentListResponse getTodayMoments(Integer userId) {
        String date = LocalDate.now().toString();
        String key = "user:" + userId + ":moment:" + date;

        try{
            Map<Object, Object> todayMoments = redisTemplate.opsForHash().entries(key);

            if (todayMoments.isEmpty()) return new TodayMomentListResponse(List.of(), 0);

            List<TodayMoment> moments = todayMoments.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> momentData = (Map<String, Object>) e.getValue();
                        return new TodayMoment(
                                e.getKey().toString(),
                                momentData.get("momentTime").toString(),
                                (List<String>) momentData.get("images"),
                                momentData.get("content").toString(),
                                (List<String>) momentData.get("tags")
                        );
                    })
                    .toList();

            return new TodayMomentListResponse(moments, moments.size());
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_READ_ERROR);
        }
    }


    /**
     * Date 날짜에 작성한 일기 개수
     * @param userId 사용자 ID
     * @param date 날짜
     * @return count
     */
    public int getTodayMomentCount(int userId, LocalDate date) {
        return todayRedisRepository.countByUserAndDate(userId, date);
    }


}
