package com.haruspeak.batch.service;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayDiaryRedisRepository;
import com.haruspeak.batch.model.repository.TodayRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryService {

    private final TodayRedisRepository todayRedisRepository;
    private final TodayDiaryRedisRepository todayDiaryRedisRepository;

    public void saveToRedis(List<TodayDiary> diaries) {
        for (TodayDiary todayDiary : diaries) {
            DailySummary summary = todayDiary.getDailySummary();
            String userId = String.valueOf(summary.getUserId());
            String date = summary.getWriteDate();

            todayRedisRepository.delete(userId, date);
            todayDiaryRedisRepository.saveTodayDiaryToRedis(userId, date, todayDiary);
        }
    }
}
