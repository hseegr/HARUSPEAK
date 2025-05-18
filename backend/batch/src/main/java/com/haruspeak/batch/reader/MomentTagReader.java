package com.haruspeak.batch.reader;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.TagRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class MomentTagReader implements ItemReader<MomentTagContext> {

    private final TagRedisService service;
    private final String date;
    private int count;

    public MomentTagReader(TagRedisService service, String date) {
        this.service = service;
        this.date = date;
        this.count = 0;
    }

    @Override
    public MomentTagContext read() throws Exception {
        try {
            MomentTagContext context =  service.popByDate(date);
            if (context == null) {
                log.info("🐛 [READER: {}] 오늘의 순간 일기 태그 조최 - {}건", date, count);
                return null;
            }
            count++;
            return context;

        } catch (Exception e) {
            log.error("💥 Tag 조회 중 예외 발생", e);
            throw new RuntimeException("순간일기 태그 STEP DATA 조회 중 예외 발생", e);
        }
    }
}
