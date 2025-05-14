package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.service.TodayDiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DailySummaryWriter implements ItemWriter<TodayDiary> {

    private final DailySummaryRepository dailySummaryRepository;
    private final DailyMomentRepository dailyMomentRepository;

    private final TodayDiaryService todayDiaryService;

    @Override
    public void write(Chunk<? extends TodayDiary> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기/순간 일기 저장");

        List<TodayDiary> diaries = (List<TodayDiary>) chunk.getItems();

        List<DailySummary> summaries = new ArrayList<>();
        List<DailyMoment> moments = new ArrayList<>();

        for (TodayDiary todayDiary : diaries) {
            summaries.add(todayDiary.getDailySummary());
            moments.addAll(todayDiary.getDailyMoments());
        }

        try {
            dailySummaryRepository.bulkInsertDailySummaries(summaries);
            dailyMomentRepository.bulkInsertDailyMoments(moments);

            todayDiaryService.saveToRedis(diaries);
            log.debug("🐛 [WRITER] 오늘의 하루 일기/순간 일기 저장 완료");

        }catch (Exception e){
            log.error("💥 오늘의 하루 일기/순간 저장 작업 중 에러가 발생했습니다.", e);
            throw new RuntimeException("하루 일기 저장 중 에러 발생", e);
        }
    }

}

