package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayDiaryRedisKeyService;
import com.haruspeak.batch.service.redis.TodayRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayMomentReader implements ItemReader<TodayDiaryContext> {

    private final TodayDiaryRedisKeyService keyService;
    private final TodayRedisService service;
    private final String date;

    public TodayMomentReader(TodayDiaryRedisKeyService keyService, TodayRedisService service, String date) {
        this.keyService = keyService;
        this.service = service;
        this.date = date;
    }

    @Override
    public TodayDiaryContext read() throws Exception {
        String key = keyService.popOneKey();

        if(key == null) {
            return null;
        }

        keyService.pushProcessingKey(key);
        log.debug("🐛 [READER] 오늘의 순간 일기 전체 조회 - {}", key);

        try {
            TodayDiaryContext diary = service.getTodayMomentsByKey(key, date);
            if (diary == null) {
                log.warn("⛔ getTodayMomentsByKey()가 null 반환! key={}, date={}", key, date);
            }
            return diary;
        } catch (Exception e) {
            log.error("💥 Today 조회 중 예외 발생 key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
