package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.context.result.SummaryProcessingResult;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.service.SummaryService;
import com.haruspeak.batch.service.redis.TodayDiaryRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class DailySummaryWriter implements ItemWriter<TodayDiaryContext> {

    private final SummaryService todaySummaryService;
    private final TodayDiaryRedisService redisService;
    private final DailySummaryRepository dailySummaryRepository;

    @Override
    public void write(Chunk<? extends TodayDiaryContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 실행");

        SummaryProcessingResult result = getTodayDiariesWithSummary((List<TodayDiaryContext>) chunk.getItems());

        List<DailySummary> summaries = result.successList().stream()
                .map(TodayDiaryContext::getDailySummary).toList();

        try {
            List<TodayDiaryContext> failedList = result.failedList();
            if(failedList!=null && !failedList.isEmpty()){
                String date = failedList.get(0).getDailySummary().getWriteDate();
                redisService.pushAll(failedList, date);
            }
            dailySummaryRepository.bulkInsertDailySummariesWithoutThumbnail(summaries);
            log.debug("🐛 [WRITER] 오늘의 하루 일기 요약 저장 완료");

        }catch (Exception e){
            log.error("💥 오늘의 하루 일기 저장 작업 중 에러 발생", e);
            throw new RuntimeException("하루 일기 저장 중 에러 발생", e);
        }
    }


    private SummaryProcessingResult getTodayDiariesWithSummary(List<TodayDiaryContext> diaries) {
        List<TodayDiaryContext> successList = new ArrayList<>();
        List<TodayDiaryContext> failedList = new ArrayList<>();
         diaries.parallelStream().forEach(todayDiary -> {
            try {
                String totalContent = buildTotalContent(todayDiary.getDailyMoments());
                log.debug("🔎 userId: {} totalContent: {} ...", todayDiary.getDailySummary().getUserId(), totalContent.substring(0, Math.min(totalContent.length(), 10)));

                DailySummaryResponse summaries = todaySummaryService.generateDailySummary(totalContent);
                log.debug("🔎 userId:{}, {}",todayDiary.getDailySummary().getUserId(), summaries.toString());
                setDailySummary(todayDiary.getDailySummary(), summaries);
                successList.add(todayDiary);

            }catch (Exception e) {
                log.warn("⚠️ 요약 처리 실패 - userId: {}, date: {}", todayDiary.getDailySummary().getUserId(), todayDiary.getDailySummary().getWriteDate(), e);
                failedList.add(new TodayDiaryContext(todayDiary.getDailySummary(), todayDiary.getDailyMoments()));
                todayDiary.setDailySummary(null);
            }
         });
         return new SummaryProcessingResult(successList, failedList);
    }


    private String buildTotalContent(List<DailyMoment> moments) {
        return moments.stream()
                .map(DailyMoment::getContent)
                .collect(Collectors.joining("\n\n"));
    }

    private void setDailySummary(DailySummary dailySummary, DailySummaryResponse summaries) {
        dailySummary.setSummaries(summaries.title(), summaries.summary());
    }

}

