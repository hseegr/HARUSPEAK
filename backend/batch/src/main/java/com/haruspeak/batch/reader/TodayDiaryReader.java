package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.service.redis.TodayDiaryRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
public class TodayDiaryReader implements ItemReader<TodayDiary> {

    private final TodayDiaryRedisService service;
    private final String date;

    public TodayDiaryReader(TodayDiaryRedisService service, String date) {
        this.service = service;
        this.date = date;
    }

    @Override
    public TodayDiary read() throws Exception {
        log.debug("🐛 [READER] 오늘의 일기 조회");
        try {
            return service.popByDate(date);

        } catch (Exception e) {
            log.error("💥 오늘의 일기 조회 중 예외 발생", e);
            throw new RuntimeException();
        }



    }
}
