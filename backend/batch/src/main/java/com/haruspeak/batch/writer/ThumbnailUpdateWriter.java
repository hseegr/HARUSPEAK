package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.dto.context.ThumbnailProcessingResult;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.service.ThumbnailGenerateService;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class ThumbnailUpdateWriter implements ItemWriter <ThumbnailGenerateContext> {

    private final ThumbnailGenerateService thumbnailGenerateService;
    private final ThumbnailRedisService redisService;
    private final DailySummaryRepository summaryRepository;

    @Override
    public void write(Chunk<? extends ThumbnailGenerateContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 썸네일 생성 및 업데이트 실행");
        List<ThumbnailGenerateContext> contexts = (List<ThumbnailGenerateContext>)chunk.getItems();
        ThumbnailProcessingResult result = thumbnailGenerateService.generateThumbnailUrlInParallel(contexts);

        try {
            List<ThumbnailGenerateContext> failedList = result.failedList();
            if(failedList!=null && !failedList.isEmpty()){
                String date = failedList.get(0).writeDate();
                redisService.pushAll(failedList, date);
            }

            summaryRepository.bulkUpdateThumbnailForDailySummaries(result.successList());

        } catch (Exception e) {
            log.error("💥 오늘의 하루 일기 썸네일 생성 및 업데이트 실패", e);
            throw new RuntimeException("💥오늘의 하루 일기 썸네일 생성 및 업데이트 실패", e);
        }

        log.debug("✅ 썸네일 생성 성공 {}건, 실패 {}건", result.successList().size(), chunk.size() - result.successList().size());

    }
}
