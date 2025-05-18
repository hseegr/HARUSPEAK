package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.service.DiaryStepContextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@StepScope
public class DailyMomentWriter implements ItemWriter<TodayDiaryContext> {

    private final DailyMomentRepository dailyMomentRepository;
    private final DiaryStepContextBuilder diaryStepContextBuilder;

    @Override
    public void write(Chunk<? extends TodayDiaryContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 순간 일기 저장 실행");

        try {
            List<TodayDiaryContext> diaries = (List<TodayDiaryContext>) chunk.getItems();

            List<TodayDiaryContext> successDiaries = new ArrayList<>();
            List<DailyMoment> moments = new ArrayList<>();

            for (TodayDiaryContext diary : diaries) {
                if (diary.getDailySummary() != null) {
                    successDiaries.add(diary);
                    moments.addAll(diary.getDailyMoments());
                }
            }

            if(moments.isEmpty()) {
                log.debug("⛔ 저장할 순간 일기 없음 - 스킵");
                return;
            }

            dailyMomentRepository.bulkInsertDailyMoments(moments);
            diaryStepContextBuilder.saveToRedis(successDiaries);

        }catch (Exception e){
            log.error("💥 오늘의 순간 일기 저장 작업 중 에러가 발생했습니다.", e);
            throw new RuntimeException("순간 일기 저장 중 에러 발생", e);
        }
    }

}

