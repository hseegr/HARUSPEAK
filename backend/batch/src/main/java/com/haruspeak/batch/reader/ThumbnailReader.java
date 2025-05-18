package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class ThumbnailReader implements ItemReader<ThumbnailGenerateContext> {

    private final ThumbnailRedisService service;
    private final String date;
    private int count;

    public ThumbnailReader(ThumbnailRedisService service, String date) {
        this.service = service;
        this.date = date;
        this.count = 0;
    }

    @Override
    public ThumbnailGenerateContext read() throws Exception {
        try {
            ThumbnailGenerateContext context =  service.popByDate(date);
            if (context == null) {
                log.info("🐛 [READER: {}] 오늘의 일기 썸네일 STEP DATA 조최 - {}건", date, count);
                return null;
            }
            count++;
            return context;

        } catch (Exception e) {
            log.error("💥 오늘의 썸네일 STEP DATA 조회 중 예외 발생", e);
            throw new RuntimeException("오늘의 썸네일 STEP DATA 조회 중 예외 발생", e);
        }
    }
}
