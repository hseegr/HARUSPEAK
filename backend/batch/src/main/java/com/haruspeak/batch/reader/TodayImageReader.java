package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.MomentImage;
import com.haruspeak.batch.service.redis.ImageRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.List;

@Slf4j
public class TodayImageReader implements ItemReader<List<MomentImage>> {

    private final ImageRedisService service;
    private final String date;

    public TodayImageReader(ImageRedisService service, String date) {
        this.service = service;
        this.date = date;
    }

    @Override
    public List<MomentImage> read() throws Exception {
        log.debug("🐛 [READER] 오늘의 순간 일기 이미지 전체 조회");

        try {
            return service.popByDate(date);

        } catch (Exception e) {
            log.error("💥 Image 조회 중 예외 발생", e);
            throw new RuntimeException();
        }
    }
}
