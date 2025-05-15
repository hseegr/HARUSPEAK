package com.haruspeak.batch.processor;

import com.haruspeak.batch.common.client.fastapi.DailySummaryClient;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.service.TodaySummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TodaySummaryProcessor implements ItemProcessor <TodayDiary, TodayDiary>{

    private final TodaySummaryService todaySummaryService;

    public TodaySummaryProcessor(TodaySummaryService todaySummaryService) {
        this.todaySummaryService = todaySummaryService;
    }

    @Override
    public TodayDiary process(TodayDiary todayDiary) throws Exception {
        log.debug("🐛 [PROCESSOR] - 오늘 하루 일기 요약 생성");

        try {
            String totalContent = buildTotalContent(todayDiary.getDailyMoments());
            log.debug("🔎 totalContent={} ...", totalContent.substring(0, 10));

            DailySummaryResponse summaries = todaySummaryService.generateDailySummary(totalContent);
            log.debug("🔎 {}", summaries.toString());

            setDailySummary(todayDiary.getDailySummary(), summaries);
            return todayDiary;

        }catch (Exception e) {
            throw new RuntimeException("오늘 하루 일기 요약 및 썸네일 생성 중 오류 발생", e);
        }
    }

    private String buildTotalContent(List<DailyMoment> moments) {
        return moments.stream()
                .map(DailyMoment::getContent)
                .collect(Collectors.joining());
    }

    private void setDailySummary(DailySummary dailySummary, DailySummaryResponse summaries) {
        dailySummary.setSummaries(summaries.title(), summaries.summary());
    }



}
