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

//    @Value("#{jobParameters['date']}") String date;
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
        log.debug("🐛 STEP1.READ - 오늘의 순간 일기 전체 조회");

        if (keyIterator == null) {
            Set<String> keys = repository.getAllKeys(date);
            this.keyIterator = keys.iterator();
            log.debug("🔍 key list = {}", String.join(", ", keys));
        }

        if (!keyIterator.hasNext()) {
            return null;
        }
        String key = keyIterator.next();
        log.debug("🔎 현재 key = {}", key);
//        return repository.getTodayMomentsByKey(keyIterator.next(), date);
        try {
            TodayDiary diary = repository.getTodayMomentsByKey(key, date);
            if (diary == null) {
                log.warn("⛔ getTodayMomentsByKey()가 null 반환! key={}, date={}", key, date);
            } else {
                log.debug("📘 diary 정상 반환됨 = {}", diary);
            }
            return diary;
        } catch (Exception e) {
            log.error("💥 diary 조회 중 예외 발생! key={}, date={}, error={}", key, date, e.getMessage(), e);
            throw e; // 재던지기
        }
    }
}
