package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayMomentTargetUserReader implements ItemReader<TodayDiaryContext> {

    private final TodayRedisService service;
    private final String date;
    private final String userId;

    public TodayMomentTargetUserReader(TodayRedisService service,
                                       String userId,
                                       String date) {
        this.service = service;
        this.date = date;
        this.userId = userId;
    }

    @Override
    public TodayDiaryContext read() throws Exception {
        log.debug("🐛 [READER] 순간 일기 조회 - userId: {}, date:{}", userId, date);

        try {
            TodayDiaryContext diary = service.getTodayMomentsByUserAndDate(userId, date);
            if (diary == null) {
                log.warn("⛔ getTodayMomentsByUserAndDate()가 null 반환! userId={}, date={}", userId, date);
            }
            return diary;
        } catch (Exception e) {
            log.error("💥 Today 조회 중 예외 발생 userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
