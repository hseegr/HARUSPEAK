package com.haruspeak.batch.service;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayDiaryDeleteService {

    private final TodayRedisService todayRedisService;

    public void deleteSummaryStepData(List<TodayDiaryContext> diaries, String date){
        for (TodayDiaryContext todayDiary : diaries) {
            DailySummary summary = todayDiary.getDailySummary();
            String userId = String.valueOf(summary.getUserId());
            todayRedisService.delete(userId, date);
        }
    }

}
