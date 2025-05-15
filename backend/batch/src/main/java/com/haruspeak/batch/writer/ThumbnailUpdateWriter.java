package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.ThumbnailUpdateDTO;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ThumbnailUpdateWriter implements ItemWriter <ThumbnailUpdateDTO> {

    private final DailySummaryRepository dailySummaryRepository;

    @Override
    public void write(Chunk<? extends ThumbnailUpdateDTO> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 썸네일 업데이트 실행");
        try {
            List<ThumbnailUpdateDTO> thumbnails = (List<ThumbnailUpdateDTO>) chunk.getItems();
            dailySummaryRepository.bulkUpdateThumbnailForDailySummaries(thumbnails);
            log.debug("🐛 [WRITER] 오늘의 하루 일기 썸네일 업데이트 완료");

        }catch (Exception e){
            log.error("💥  오늘의 하루 일기 썸네일 업데이트 중 에러가 발생했습니다.", e);
            throw new RuntimeException(" 오늘의 하루 일기 썸네일 업데이트 중 에러 발생", e);
        }
    }
}
