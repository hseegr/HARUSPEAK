package com.haruspeak.batch.reader;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayDiaryRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@Component
public class TodayDiaryReader implements ItemReader<TodayDiary> {

    private final TodayDiaryRedisRepository repository;
    private final String date;

    private Iterator<String> keyIterator;

    //    @Value("#{jobParameters['date']}") String date;
    public TodayDiaryReader(TodayDiaryRedisRepository repository, String date) {
        this.repository = repository;
        this.date = date;
    }

    @Override
    public TodayDiary read() throws Exception {
        log.debug("🐛 STEP2/3.READ - 오늘의 일기 조회 FOR Tags Step");

        if (keyIterator == null) {
            keyIterator = repository.getAllKeys(date).iterator();
        }

        if (!keyIterator.hasNext()) {
            return null;
        }

        return repository.getTodayDiaryByKey(keyIterator.next());
    }
}
