package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailySummaryWriter implements ItemWriter<TodayDiary> {

    private final DailySummaryRepository dailySummaryRepository;
    private final DailyMomentRepository dailyMomentRepository;

    @Override
    public void write(Chunk<? extends TodayDiary> chunk) throws Exception {
        log.debug("🐛 STEP1.WRITE - 오늘의 하루 일기/순간 일기 저장");

        List<DailySummary> summaries = new ArrayList<>();
        List<DailyMoment> moments = new ArrayList<>();

        for (TodayDiary todayDiary : chunk.getItems()) {
            summaries.add(todayDiary.getDailySummary());
            moments.addAll(todayDiary.getDailyMoments());
        }

        try {
            dailySummaryRepository.bulkInsertDailySummaries(summaries);
            dailyMomentRepository.bulkInsertDailyMoments(moments);

        }catch (Exception e){
            log.error("⚠️ daily_summary, daily_moments 삽입 중 에러가 발생했습니다.", e);
            throw e;
        }
    }
}

