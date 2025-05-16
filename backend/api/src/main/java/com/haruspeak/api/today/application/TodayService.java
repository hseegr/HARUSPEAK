package com.haruspeak.api.today.application;


import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.s3.S3Service;
import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.today.dto.TodayMoment;
import com.haruspeak.api.today.dto.request.MomentUpdateRequest;
import com.haruspeak.api.today.dto.request.MomentWriteRequest;
import com.haruspeak.api.today.dto.request.TagUpdateRequest;
import com.haruspeak.api.today.dto.response.TodayMomentListResponse;
import com.haruspeak.api.today.domain.repository.TodayMomentRedisStringRepository;
import com.haruspeak.api.today.dto.response.TodaySttResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayService {

    private final TodayMomentRedisStringRepository todayRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final S3Service s3Service;
    private final FastApiClient fastApiClient;

    public TodaySttResponse transferStt(
            String uri,
            MultipartFile file,
            Integer userId
    ) {
        return fastApiClient.gpuConvertVoiceToText(uri, file, TodaySttResponse.class);
    }

    /**
     * 순간 일기 작성
     * @param request
     * @param userId
     */
    public void saveMoment(MomentWriteRequest request, Integer userId) {

        if (request.content().trim().isEmpty() && request.images().isEmpty()) throw new HaruspeakException(ErrorCode.BLANK_MOMENT);
        LocalDateTime now = LocalDateTime.now();
        String key = redisKey(userId, now);

        List<String> images = request.images().stream()
                .map(s3Service::uploadImagesAndGetUrls)
                .toList();

        Map<String, Object> momentData = new HashMap<>();
        momentData.put("momentTime", now.toString());
        momentData.put("content", request.content());
        momentData.put("images", images);
        momentData.put("tags", List.of());

        try {
            redisTemplate.opsForHash().put(key, now.toString(), momentData);
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_SAVE_ERROR, e.getMessage());
        }
    }

    /**
     * 순간 일기 수정
     * @param time
     * @param request
     * @param userId
     */
    public void updateMoment(String time, MomentUpdateRequest request, Integer userId){

        String key = redisKey(userId, LocalDateTime.now());
        Map<String, Object> existingMoment = (Map<String, Object>) redisTemplate.opsForHash().get(key, time);

        if (existingMoment == null) throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);
        if (request.content().trim().isEmpty() && request.images().isEmpty()) throw new HaruspeakException(ErrorCode.BLANK_MOMENT);
        if (request.content().length()>500) throw new HaruspeakException(ErrorCode.INVALID_MOMENT_CONTENT_LENGTH);

        validateTags(request.tags());
        validateDeletedImages(request.images(),request.deletedImages());

        try {
            request.deletedImages().forEach(s3Service::deleteImages);

            List<String> images = request.images().stream()
                    .map(image -> image.startsWith("data:image")
                            ? s3Service.uploadImagesAndGetUrls(image)
                            : image)
                    .toList();

            existingMoment.put("momentTime", request.momentTime());
            existingMoment.put("content", request.content());
            existingMoment.put("images", images);
            existingMoment.put("tags", request.tags());

            redisTemplate.opsForHash().put(key, time, existingMoment);

        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_UPDATE_ERROR, e.getMessage());
        }
    }

    /**
     * 오늘 순간 일기 삭제
     * @param time
     * @param userId
     */
    public void removeMoment(String time, Integer userId){

        String key = redisKey(userId, LocalDateTime.now());

        try {
            Map<String, Object> moment = (Map<String, Object>) redisTemplate.opsForHash().get(key, time);

            if (moment == null) throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);

            ofNullable((List<String>) moment.get("images"))
                    .ifPresent(images -> images.forEach(s3Service::deleteImages));

            redisTemplate.opsForHash().delete(key, time);

        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_DELETE_ERROR, e.getMessage());
        }
    }

    /**
     * 오늘의 순간 일기들 불러오기
     * @param userId
     * @return
     */
    public TodayMomentListResponse getTodayMoments(Integer userId) {

        String key = redisKey(userId, LocalDateTime.now());

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
                    .sorted((a, b) -> b.momentTime().compareTo(a.momentTime()))
                    .toList();

            return new TodayMomentListResponse(moments, moments.size());
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_READ_ERROR, e.getMessage());
        }
    }

    /**
     * 태그 업데이트
     * @param request 태그 업데이트 요청
     * @param userId 유저ID
     */
    public void updateTag(TagUpdateRequest request, Integer userId){
        
        String key = redisKey(userId, LocalDateTime.now());

        try{
            Map<String, Object> existingMoment = (Map<String, Object>) redisTemplate.opsForHash().get(key, request.createdAt());

            if (existingMoment == null) throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);

            validateTags(request.tags());
            existingMoment.put("tags", request.tags());

            redisTemplate.opsForHash().put(key, request.createdAt(), existingMoment);
        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.MOMENT_UPDATE_ERROR, e.getMessage());
        }
    }


    /**
     * Date 날짜에 작성한 일기 개수
     * @param userId 사용자 ID
     * @param date 날짜
     * @return count
     */
    public long getTodayMomentCount(int userId, LocalDate date) {
        return todayRedisRepository.countByUserAndDate(userId, date);
    }

    /**
     * redis key 반환 함수
     * @param userId
     * @return
     */
    public String redisKey(Integer userId, LocalDateTime now){
        return "user:" + userId + ":moment:" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * tag 검사 메서드
     * @param tags
     */
    private void validateTags(List<String> tags){
        if (tags == null) return;
        if (tags.size() > 10) {
            throw new HaruspeakException(ErrorCode.INVALID_MOMENT_TAG_SIZE);
        }

        Set<String> seenTags = new HashSet<>();

        for (String tag : tags) {
            if (tag == null || tag.trim().isEmpty()) {
                throw new HaruspeakException(ErrorCode.INVALID_MOMENT_TAG_FORMAT);
            }
            if (tag.length() > 10) {
                throw new HaruspeakException(ErrorCode.INVALID_MOMENT_TAG_LENGTH);
            }
            if (!tag.matches("^[a-zA-Z0-9 _가-힣]+$")) {
                throw new HaruspeakException(ErrorCode.INVALID_MOMENT_TAG_CHARACTER);
            }
            if (!seenTags.add(tag)) {
                throw new HaruspeakException(ErrorCode.DUPLICATION_TAG);
            }
        }
    }

    /**
     * 이미지와 삭제할 이미지 검사 메서드
     * @param images
     * @param deletedImages
     */
    private void validateDeletedImages(List<String> images, List<String> deletedImages){
        for(String image:images){
            if(deletedImages.contains(image)){
                throw new HaruspeakException(ErrorCode.DUPLICATION_DELETE_IMAGE);
            }
        }
    }
}
