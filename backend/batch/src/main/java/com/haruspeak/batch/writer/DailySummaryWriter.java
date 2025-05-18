package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.DailySummaryStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class DailySummaryWriter implements ItemWriter<TodayDiaryContext> {

    private final DailySummaryStoreService dailySummaryStoreService;

    @Override
    public void write(Chunk<? extends TodayDiaryContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 실행");

        try {
            List<TodayDiaryContext> contexts = (List<TodayDiaryContext>) chunk.getItems();
            dailySummaryStoreService.processAndStoreSummaries(contexts);

            log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 완료");

        }catch (Exception e){
            log.error("💥 오늘의 하루 일기 저장 작업 중 에러 발생", e);
            throw new RuntimeException("하루 일기 저장 중 에러 발생", e);
        }
    }

}

