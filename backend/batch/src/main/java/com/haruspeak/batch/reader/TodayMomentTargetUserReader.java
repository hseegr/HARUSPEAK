package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayMomentRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayMomentTargetUserReader implements ItemReader<TodayDiaryContext> {

    private final TodayMomentRedisService service;
    private final String date;
    private final String userId;
    private boolean isExecuted;

    public TodayMomentTargetUserReader(TodayMomentRedisService service,
                                       String userId,
                                       String date) {
        this.service = service;
        this.date = date;
        this.userId = userId;
        this.isExecuted = false;
    }

    @Override
    public TodayDiaryContext read() throws Exception {
        try {
            if(!isExecuted) {
                TodayDiaryContext diary = service.getTodayMomentsByUserAndDate(userId, date);
                if (diary == null) {
                    log.warn("⛔ getTodayMomentsByUserAndDate()가 null 반환! userId={}, date={}", userId, date);
                }
                log.info("🐛 [READER: {}] 순간 일기 조회 - userId: {}", date, userId);
                isExecuted = true;
                return diary;
            }
            return  null;

        } catch (Exception e) {
            log.error("💥 Today 조회 중 예외 발생 userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
