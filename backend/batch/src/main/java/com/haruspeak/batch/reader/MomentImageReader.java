package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.MomentImageContext;
import com.haruspeak.batch.service.redis.ImageRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class MomentImageReader implements ItemReader<MomentImageContext> {

    private final ImageRedisService service;
    private final String date;
    private int count;

    public MomentImageReader(ImageRedisService service, String date) {
        this.service = service;
        this.date = date;
        this.count = 0;
    }

    @Override
    public MomentImageContext read() throws Exception {
        try {
            MomentImageContext context =  service.popByDate(date);
            if (context == null) {
                log.info("🐛 [READER: {}] 오늘의 순간 일기 이미지 조최 - {}건", date, count);
                return null;
            }
            count++;
            return context;

        } catch (Exception e) {
            log.error("💥 Image 조회 중 예외 발생", e);
            throw new RuntimeException("순간일기 이미지 STEP DATA 조회 중 예외 발생", e);
        }
    }
}
