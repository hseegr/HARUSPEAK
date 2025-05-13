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
        log.debug("🐛 STEP1.PROCESS - 오늘 하루 일기 요약 및 썸네일 생성");

        String totalContent = buildTotalContent(todayDiary.getDailyMoments());

        DailySummaryResponse summaries = todaySummaryService.generateDailySummary(totalContent);
        String imageUrl = todaySummaryService.generateThumbnailUrl(summaries.summary());

        setDailySummary(todayDiary.getDailySummary(), summaries, imageUrl);
        return  todayDiary;
    }

    private String buildTotalContent(List<DailyMoment> moments) {
        return moments.stream()
                .map(DailyMoment::getContent)
                .collect(Collectors.joining());
    }

    private void setDailySummary(DailySummary dailySummary, DailySummaryResponse summaries, String imageUrl) {
        dailySummary.setSummaries(summaries.title(), summaries.summary(), imageUrl);
    }



}
