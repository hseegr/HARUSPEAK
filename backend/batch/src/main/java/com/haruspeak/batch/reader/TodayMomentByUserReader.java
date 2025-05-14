package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.Set;

@Slf4j
public class TodayMomentByUserReader implements ItemReader<TodayDiary> {

    private final TodayRedisRepository repository;
    private final String date;
    private final String userId;
    private boolean isExecuted;

    public TodayMomentByUserReader(TodayRedisRepository repository, String date, String userId, boolean isExecuted) {
        this.repository = repository;
        this.date = date;
        this.userId = userId;
        this.isExecuted = isExecuted;
    }

    /**
     * Map Key : Redis Key,
     * Map Value - Key : Redis Field ( createdAt )
     * Map Value - Value : Redis Value ( TodayMoment )
     */
    @Override
    public TodayDiary read() throws Exception {
        if(isExecuted) return null;

        log.debug("🐛 [READER] 오늘의 순간 일기 조회 - userId = {}, date = {}", userId, date);

        String key  = repository.getKey(userId, date);
        log.debug("🔎 key = {}", key);

        try {
            TodayDiary diary = repository.getTodayMomentsByKey(key, date);
            if (diary == null) {
                log.warn("⛔ 기록된 오늘의 일기가 없습니다. key={}, date={}", key, date);
            } else {
                log.debug("📘 diary.summary = {}", diary.getDailySummary());
            }

            isExecuted = true;
            return diary;
        } catch (Exception e) {
            log.error("💥 diary 조회 중 예외 발생! key={}, date={}, error={}", key, date, e.getMessage(), e);
            throw e;
        }
    }
}
