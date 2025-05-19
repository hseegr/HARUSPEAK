package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.context.MomentImageContext;
import com.haruspeak.batch.model.repository.MomentImageRepository;
import com.haruspeak.batch.service.redis.ImageRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MomentImageWriter implements ItemWriter<MomentImageContext> {

    private final ImageRedisService imageRedisService;
    private final MomentImageRepository momentImageRepository;

    @Override
    public void write(Chunk<? extends MomentImageContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 일기 이미지 저장");

        List<MomentImageContext> momentImages = (List<MomentImageContext>)chunk.getItems();
        try {
            momentImageRepository.bulkInsertMomentImages(momentImages);

        }catch (Exception e){
            log.error("💥 moment_images 삽입 중 에러가 발생했습니다.", e);
            imageRedisService.pushAll(momentImages.get(0).getCreatedAt().substring(0,10), momentImages);
            throw new RuntimeException("순간 일기 이미지 저장 중 에러 발생", e);
        }
    }

}

