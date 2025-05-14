package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayDiaryRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.Set;

@Slf4j
public class TodayDiaryReader implements ItemReader<TodayDiary> {

    private final TodayDiaryRedisRepository repository;
    private final String date;

    private Iterator<String> keyIterator;

    public TodayDiaryReader(TodayDiaryRedisRepository repository, String date) {
        this.repository = repository;
        this.date = date;
    }

    @Override
    public TodayDiary read() throws Exception {
        if (keyIterator == null) {
            Set<String> keys = repository.getAllKeys(date);
            this.keyIterator = keys.iterator();
        }

        if (!keyIterator.hasNext()) {
            log.debug("🐛 오늘의 다이어리 조회 커서 종료");
            return null;
        }

        String key = keyIterator.next();
        log.debug("🐛 [READER] 오늘의 요약/순간 일기 조회 - {}", key);

        try {
            return repository.getTodayDiaryByKey(key);
        } catch (Exception e) {
            log.error("💥 Diary 조회 중 예외 발생 key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException(e);
        }



    }
}
