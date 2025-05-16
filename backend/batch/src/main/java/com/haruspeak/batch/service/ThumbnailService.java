package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final ThumbnailRedisService redisService;

    public void saveThumbnailStepData (List<TodayDiaryContext> diaries, String date) {
        log.debug("🐛 썸네일 STEP DATA REDIS 저장 실행");
        try {
            List<ThumbnailGenerateContext> contexts = retrieveThumbnailStepData(diaries, date);
            log.debug("THUMBNAIL STEP DATA: {}건", contexts.size());
            redisService.pushAll(contexts, date);    
        } catch (Exception e) {
            throw new RuntimeException("💥 썸네일 STEP DATA REDIS 저장 실패", e);
        }
        
    }

    private List<ThumbnailGenerateContext> retrieveThumbnailStepData (List<TodayDiaryContext> diaries, String date) {
        return diaries.stream()
                .map(diary -> {
                    DailySummary summary = diary.getDailySummary();
                    return new ThumbnailGenerateContext(summary.getUserId(), date, summary.getContent());
                }).toList();
    }
}
