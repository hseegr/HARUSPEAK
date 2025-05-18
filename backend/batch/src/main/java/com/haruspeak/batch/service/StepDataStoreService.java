package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.DiaryStepContext;
import com.haruspeak.batch.service.redis.ImageRedisService;
import com.haruspeak.batch.service.redis.TagRedisService;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepDataStoreService {

    private final TagRedisService tagRedisService;
    private final ImageRedisService imageRedisService;
    private final ThumbnailRedisService thumbnailRedisService;

    public void storeAll(String date, DiaryStepContext context) {
        if (!context.tagContexts().isEmpty()) {
            tagRedisService.pushAll(date, context.tagContexts());
        }
        if (!context.imageContexts().isEmpty()) {
            imageRedisService.pushAll(date, context.imageContexts());
        }
        if (!context.thumbnailContexts().isEmpty()) {
            thumbnailRedisService.pushAll(date, context.thumbnailContexts());
        }
    }
}