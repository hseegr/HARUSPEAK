package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiaryTag;
import com.haruspeak.batch.service.redis.TagRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayTagReader implements ItemReader<TodayDiaryTag> {

    private final TagRedisService service;
    private final String date;

    public TodayTagReader(TagRedisService service, String date) {
        this.service = service;
        this.date = date;
    }

    @Override
    public TodayDiaryTag read() throws Exception {
        log.debug("🐛 [READER] 오늘의 순간 일기 태그 전체 조회");
        try {
            return service.popByDate(date);

        } catch (Exception e) {
            log.error("💥 Image 조회 중 예외 발생", e);
            throw new RuntimeException();
        }
    }
}
