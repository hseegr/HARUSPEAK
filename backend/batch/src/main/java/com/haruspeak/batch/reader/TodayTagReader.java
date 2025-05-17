package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.TodayDiaryTagContext;
import com.haruspeak.batch.service.redis.TagRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayTagReader implements ItemReader<TodayDiaryTagContext> {

    private final TagRedisService service;
    private final String date;

    public TodayTagReader(TagRedisService service, String date) {
        this.service = service;
        this.date = date;
    }

    @Override
    public TodayDiaryTagContext read() throws Exception {
        log.debug("🐛 [READER] 오늘의 순간 일기 태그 전체 조회");
        try {
            TodayDiaryTagContext context = service.popByDate(date);
            if (context == null) {
                log.debug("🚫 더 이상 남은 데이터 없음, Step 종료");
                return null;
            }
            log.debug("📦 READ 데이터: {}", context);
            return context;

        } catch (Exception e) {
            log.error("💥 Tag 조회 중 예외 발생", e);
            throw new RuntimeException();
        }
    }
}
