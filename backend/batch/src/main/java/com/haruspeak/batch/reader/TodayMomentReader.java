package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Slf4j
public class TodayMomentReader implements ItemReader<TodayDiary> {

    private final TodayRedisRepository repository;
    private final String date;

    private Iterator<String> keyIterator;

    public TodayMomentReader(TodayRedisRepository repository, String date) {
        this.repository = repository;
        this.date = date;
    }

    /**
     * Map Key : Redis Key,
     * Map Value - Key : Redis Field ( createdAt )
     * Map Value - Value : Redis Value ( TodayMoment )
     */
    @Override
    public TodayDiary read() throws Exception {
        if (keyIterator == null) {
            Set<String> keys = repository.getAllKeys(date);
            this.keyIterator = keys.iterator();
        }

        if (!keyIterator.hasNext()) {
            log.debug("🐛 오늘의 순간 일기 조회 커서 종료");
            return null;
        }

        String key = keyIterator.next();
        log.debug("🐛 [READER] 오늘의 순간 일기 전체 조회 - {}", key);

        try {
            TodayDiary diary = repository.getTodayMomentsByKey(key, date);
            if (diary == null) {
                log.warn("⛔ getTodayMomentsByKey()가 null 반환! key={}, date={}", key, date);
            } else {
                log.debug("📘 diary.summary = {}", diary.getDailySummary());
            }
            return diary;
        } catch (Exception e) {
            log.error("💥 Today 조회 중 예외 발생 key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
