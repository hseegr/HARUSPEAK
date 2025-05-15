package com.haruspeak.batch.processor;

import com.haruspeak.batch.dto.ThumbnailUpdateDTO;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.service.ThumbnailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class TodayThumbnailProcessor implements ItemProcessor <TodayDiary, ThumbnailUpdateDTO>{

    private final ThumbnailService todayThumbnailService;

    public TodayThumbnailProcessor(ThumbnailService todayThumbnailService) {
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
