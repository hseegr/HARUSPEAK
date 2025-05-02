package com.haruspeak.api.moment.application;


import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.s3.FileConverter;
import com.haruspeak.api.common.s3.S3Uploader;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodayService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final S3Uploader s3Uploader;

    public void saveMoment(MomentWriteRequest request, Integer userId) {
        String date = LocalDate.now().toString();
        String key = "user:" + userId + ":moment:" + date;
        String now = LocalDateTime.now().toString();

        List<String> images = request.images().stream()
                .map(FileConverter::fromBase64)
                .map(file -> {
                    String imageKey = "uploads/" + file.getOriginalFilename();
                    return s3Uploader.uploadFile(file, imageKey);
                })
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
}
