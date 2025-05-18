package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TodayDiaryRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayDiaryReader implements ItemReader<TodayDiaryContext> {

    private final TodayDiaryRedisService service;
    private final String date;
    private int count;

    public TodayDiaryReader(TodayDiaryRedisService service, String date) {
        this.service = service;
        this.date = date;
        this.count = 0;
    }

    @Override
    public TodayDiaryContext read() throws Exception {
        try {
            TodayDiaryContext context =  service.popByDate(date);
            if (context == null) {
                log.info("🐛 [READER: {}] 오늘의 일기 조회 RETRY - {}건", date, count);
                return null;
            }
            count++;
            return context;

        } catch (Exception e) {
            log.error("💥 오늘의 일기 조회 중 예외 발생", e);
            throw new RuntimeException("오늘의 일기 조회 중 예외 발생", e);
        }
    }
}
