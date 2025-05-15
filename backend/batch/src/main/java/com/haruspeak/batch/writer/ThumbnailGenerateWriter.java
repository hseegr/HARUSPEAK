package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.ThumbnailGenerateDTO;
import com.haruspeak.batch.dto.ThumbnailUpdateContext;
import com.haruspeak.batch.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ThumbnailGenerateWriter implements ItemWriter <ThumbnailUpdateContext> {

    private final ThumbnailService thumbnailService;

    @Override
    public void write(Chunk<? extends ThumbnailUpdateContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 하루 일기 썸네일 생성 요청 실행");
        try {
            List<ThumbnailUpdateContext> contexts = (List<ThumbnailUpdateContext>) chunk.getItems();
            thumbnailService.generateThumbnailUrlInParallel(contexts);

        }catch (Exception e){
            log.error("💥  오늘의 하루 일기 썸네일 생성 중 에러가 발생했습니다.", e);
            throw new RuntimeException(" 오늘의 하루 일기 썸네일 생성 중 에러 발생", e);
        }
    }
}
