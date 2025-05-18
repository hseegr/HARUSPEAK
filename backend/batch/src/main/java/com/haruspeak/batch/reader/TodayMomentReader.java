package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayDiaryRedisKeyService;
import com.haruspeak.batch.service.redis.TodayMomentRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayMomentReader implements ItemReader<TodayDiaryContext> {

    private final TodayDiaryRedisKeyService keyService;
    private final TodayMomentRedisService service;
    private final String date;
    private int count;

    public TodayMomentReader(TodayDiaryRedisKeyService keyService, TodayMomentRedisService service, String date) {
        this.keyService = keyService;
        this.service = service;
        this.date = date;
        this.count = 0;
    }

    @Override
    public TodayDiaryContext read() throws Exception {
        String key = keyService.popOneKey();
        if(key == null) {
            log.info("🐛 [READER: {}] 오늘의 일기 조회 - {}건", date, count);
            return null;
        }

        try {
            keyService.pushProcessingKey(key);
            log.debug("일기 조회 대상 KEY - {}", key);

            TodayDiaryContext diary = service.getTodayMomentsByKey(key, date);
            if (diary == null) {
                log.warn("⛔ getTodayMomentsByKey()가 null 반환! key={}, date={}", key, date);
                return null;
            }

            count++;
            return diary;
        } catch (Exception e) {
            log.error("💥 Today 조회 중 예외 발생 key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
