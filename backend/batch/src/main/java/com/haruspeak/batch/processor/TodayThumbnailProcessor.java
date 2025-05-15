package com.haruspeak.batch.processor;

import com.haruspeak.batch.dto.ThumbnailUpdateDTO;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.service.TodaySummaryService;
import com.haruspeak.batch.service.TodayThumbnailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TodayThumbnailProcessor implements ItemProcessor <TodayDiary, ThumbnailUpdateDTO>{

    private final TodayThumbnailService todayThumbnailService;

    public TodayThumbnailProcessor(TodayThumbnailService todayThumbnailService) {
        this.todayThumbnailService = todayThumbnailService;
    }

    @Override
    public ThumbnailUpdateDTO process(TodayDiary diary) throws Exception {
        log.debug("🐛 [PROCESSOR] - 오늘의 일기 썸네일 생성");

        try {
            DailySummary summary = diary.getDailySummary();
            String imageUrl = todayThumbnailService.generateThumbnailUrl(summary.getContent());
            return new ThumbnailUpdateDTO(
                    summary.getUserId(),
                    summary.getWriteDate(),
                    imageUrl
            );

        }catch (Exception e) {
            log.error("💥  오늘의 일기 썸네일 생성 중 오류 발생했습니다.", e);
            throw new RuntimeException("오늘의 일기 썸네일 생성 중 오류 발생", e);
        }
    }

}
