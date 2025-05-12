package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailySummaryWriter implements ItemWriter<DailySummary> {

    private final DailySummaryRepository dailySummaryRepository;

    @Override
    public void write(Chunk<? extends DailySummary> chunk) throws Exception {
        log.debug("🐛 STEP1.WRITE - 오늘의 하루 일기/순간 일기 저장");
        List<DailySummary> summaries = (List<DailySummary>) chunk.getItems();
        dailySummaryRepository.bulkInsertDailySummaries(summaries);
    }
}

