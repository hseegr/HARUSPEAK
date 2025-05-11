package com.haruspeak.batch.reader;

import com.haruspeak.batch.domain.TodayMoment;
import com.haruspeak.batch.domain.repository.TodayRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TodayItemReader implements ItemReader<Map<Integer, List<TodayMoment>>> {

    private final TodayRedisRepository repository;
    private final String date;

    private Iterator<String> keyIterator;
    /*
    @Value("#{jobParameters['date']}") String date,
     */
    public TodayItemReader(TodayRedisRepository repository, String date) {
        this.repository = repository;
        this.date = date;
    }

    /**
     * Map Key : Redis Key,
     * Map Value - Key : Redis Field ( createdAt )
     * Map Value - Value : Redis Value ( TodayMoment )
     */
    @Override
    public Map<Integer, List<TodayMoment>> read() throws Exception {
        log.debug("🐛 STEP1.READ - 오늘의 순간 일기 전체 조회");

        if (keyIterator == null) {
            keyIterator = repository.getAllKeys(date).iterator();
        }

        if (!keyIterator.hasNext()) {
            return null;
        }
        return repository.getTodayMomentsByKey(keyIterator.next());
    }
}
