package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.MomentImage;
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

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MomentImageWriter implements ItemWriter<List<MomentImage>> {

    private final ImageRedisService imageRedisService;
    private final MomentImageRepository momentImageRepository;

    @Override
    public void write(Chunk<? extends List<MomentImage>> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 일기 이미지 저장");

        List<List<MomentImage>> momentImages = (List<List<MomentImage>>)chunk.getItems();
        List<MomentImage> moments = new ArrayList<>();
        for (List<MomentImage> momentList : chunk.getItems()) {
            moments.addAll(momentList);
        }

        try {
            momentImageRepository.bulkInsertMomentImages(moments);

        }catch (Exception e){
            log.error("💥 moment_images 삽입 중 에러가 발생했습니다.", e);
            imageRedisService.pushAll(momentImages, moments.get(0).momentTime().substring(0, 11));
            throw new RuntimeException("순간 일기 이미지 저장 중 에러 발생", e);
        }
    }

}

