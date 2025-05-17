package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class ThumbnailReader implements ItemReader<ThumbnailGenerateContext> {

    private final ThumbnailRedisService service;
    private final String date;

    public ThumbnailReader(ThumbnailRedisService service, String date) {
        this.service = service;
        this.date = date;
    }

    @Override
    public ThumbnailGenerateContext read() throws Exception {
        log.debug("🐛 [READER] 오늘의 순간 일기 태그 전체 조회");
        try {
            return service.popByDate(date);

        } catch (Exception e) {
            log.error("💥 Image 조회 중 예외 발생", e);
            throw new RuntimeException();
        }
    }
}
