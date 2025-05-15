package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DailySummaryWriter implements ItemWriter<TodayDiary> {

    private final DailySummaryRepository dailySummaryRepository;

    @Override
    public void write(Chunk<? extends TodayDiary> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 실행");

        List<TodayDiary> diaries = (List<TodayDiary>) chunk.getItems();
        List<DailySummary> summaries = diaries.stream()
                .map(TodayDiary::getDailySummary).toList();


        try {
            dailySummaryRepository.bulkInsertDailySummariesWithoutThumbnail(summaries);
            log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 완료");

        }catch (Exception e){
            log.error("💥 오늘의 하루 일기 저장 작업 중 에러가 발생했습니다.", e);
            throw new RuntimeException("하루 일기 저장 중 에러 발생", e);
        }
    }

}

