package com.haruspeak.batch.processor;

import com.haruspeak.batch.domain.DailySummary;
import com.haruspeak.batch.domain.TodayMoment;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.service.TodaySummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TodaySummaryProcessor implements ItemProcessor <Map<Integer, List<TodayMoment>>, DailySummary>{

    private final TodaySummaryService todaySummaryService;
    private final String date;

    public TodaySummaryProcessor(TodaySummaryService todaySummaryService, String date) {
        this.todaySummaryService = todaySummaryService;
        this.date = date;
    }

    @Override
    public DailySummary process(Map<Integer, List<TodayMoment>> todayDiary) throws Exception {
        log.debug("🐛 STEP1.PROCESS - 오늘 하루 일기 요약 및 썸네일 생성");

        int userId = todayDiary.keySet().iterator().next();
        List<TodayMoment> moments = todayDiary.get(userId);
        StringBuilder totalContent = new StringBuilder();
        for(TodayMoment moment : moments) {
            totalContent.append(moment.getContent());
        }

        DailySummaryResponse summaries = todaySummaryService.getDailySummaryAndTitle(totalContent.toString());
        String imageUrl = todaySummaryService.getTodayThumbnailS3Url(summaries.summary());

        return new DailySummary(userId, date, summaries.title(), imageUrl, summaries.summary(), moments.size());
    }


}
