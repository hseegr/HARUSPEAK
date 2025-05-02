package com.haruspeak.api.moment.application;


import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.s3.FileConverter;
import com.haruspeak.api.common.s3.S3Service;
import com.haruspeak.api.common.s3.S3Uploader;
import com.haruspeak.api.moment.dto.request.MomentUpdateRequest;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.*;

@Service
@RequiredArgsConstructor
public class TodayService {

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
        momentData.put("tags", new String[0]);

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
}
