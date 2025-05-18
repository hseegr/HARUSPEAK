package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.dto.context.result.SummaryProcessingResult;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.service.redis.TodayDiaryRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailySummaryStoreService {

    private final SummaryService todaySummaryService;
    private final TodayDiaryRedisService redisService;
    private final DailySummaryRepository dailySummaryRepository;

    public void processAndStoreSummaries(List<TodayDiaryContext> contexts){
        SummaryProcessingResult result = todaySummaryService.getTodayDiariesWithSummary(contexts);

        List<DailySummary> summariesWithoutThumbnail = result.successList().stream()
                .map(TodayDiaryContext::getDailySummary).toList();
        dailySummaryRepository.bulkInsertDailySummariesWithoutImage(summariesWithoutThumbnail);

        List<DailySummary> summaries = result.nonContentList().stream()
                .map(TodayDiaryContext::getDailySummary).toList();
        dailySummaryRepository.bulkInsertDailySummaries(summaries);

        List<TodayDiaryContext> failedList = result.failedList();
        if(failedList!=null && !failedList.isEmpty()){
            String date = failedList.get(0).getDailySummary().getWriteDate();
            redisService.pushAll(failedList, date);
        }
    }

}
