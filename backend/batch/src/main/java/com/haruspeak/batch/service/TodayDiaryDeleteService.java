package com.haruspeak.batch.service;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayMomentRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayDiaryDeleteService {

    private final TodayMomentRedisService todayMomentRedisService;

    public void deleteSummaryStepData(String date, List<TodayDiaryContext> diaries){
        for (TodayDiaryContext todayDiary : diaries) {
            DailySummary summary = todayDiary.getDailySummary();
            String userId = String.valueOf(summary.getUserId());
            todayMomentRedisService.delete(userId, date);
        }
    }

}
