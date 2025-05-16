package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryStepDataService {

    private final TodayDiaryDeleteService todayDiaryService;
    private final TodayTagService todayTagService;
    private final TodayImageService todayImageService;
    private final ThumbnailService thumbnailService;

    public void saveToRedis(List<TodayDiaryContext> diaries) {
        String date = diaries.get(0).getDailySummary().getWriteDate();
        log.debug("🐛 TodayDiary API REDIS 삭제 및 BATCH REDIS 저장 실행 - date= {}", date);

        todayTagService.saveTagStepData(diaries, date);
        todayImageService.saveImageStepData(diaries, date);
        thumbnailService.saveThumbnailStepData(diaries, date);
//        todayDiaryService.deleteSummaryStepData(diaries, date);
    }
}
