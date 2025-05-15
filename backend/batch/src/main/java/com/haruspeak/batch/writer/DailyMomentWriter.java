package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.service.TodayDiaryStepDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@StepScope
public class DailyMomentWriter implements ItemWriter<TodayDiary> {

    private final DailyMomentRepository dailyMomentRepository;
    private final TodayDiaryStepDataService todayDiaryStepDataService;

    @Override
    public void write(Chunk<? extends TodayDiary> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 순간 일기 저장 실행");

        List<TodayDiary> diaries = (List<TodayDiary>) chunk.getItems();
        List<DailyMoment> moments = diaries.stream()
                .filter(todayDiary -> todayDiary.getDailySummary() != null)
                .flatMap(todayDiary -> todayDiary.getDailyMoments().stream())
                .toList();

        try {
            dailyMomentRepository.bulkInsertDailyMoments(moments);
            todayDiaryStepDataService.saveToRedis(diaries);

            log.debug("🐛 [WRITER] 오늘의 순간 일기 저장 완료 - {}/{}명", moments.size(), diaries.size());

        }catch (Exception e){
            log.error("💥 오늘의 순간 일기 저장 작업 중 에러가 발생했습니다.", e);
            throw new RuntimeException("순간 일기 저장 중 에러 발생", e);
        }
    }

}

