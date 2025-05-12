package com.haruspeak.batch.processor;

import com.haruspeak.batch.domain.DailySummary;
import com.haruspeak.batch.domain.DailyMoment;
import com.haruspeak.batch.domain.TodayDiary;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.service.TodaySummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TodaySummaryProcessor implements ItemProcessor <TodayDiary, TodayDiary>{

    private final TodaySummaryService todaySummaryService;
    private final String date;

    public TodaySummaryProcessor(TodaySummaryService todaySummaryService, String date) {
        this.todaySummaryService = todaySummaryService;
        this.date = date;
    }

    @Override
    public TodayDiary process(TodayDiary todayDiary) throws Exception {
        log.debug("🐛 STEP1.PROCESS - 오늘 하루 일기 요약 및 썸네일 생성");

        List<DailyMoment> moments = todayDiary.getDailyMoments();
        StringBuilder totalContent = new StringBuilder();
        for(DailyMoment moment : moments) {
            totalContent.append(moment.getContent());
        }

        DailySummaryResponse summaries = todaySummaryService.getDailySummaryAndTitle(totalContent.toString());
        String imageUrl = todaySummaryService.getTodayThumbnailS3Url(summaries.summary());

        return null;
    }


}
