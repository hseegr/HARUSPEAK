package com.haruspeak.batch.writer;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.service.TagStepService;
import com.haruspeak.batch.service.redis.TagRedisService;
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
public class MomentTagWriter implements ItemWriter<MomentTagContext>{

    private final TagRedisService tagRedisService;
    private final TagStepService tagStepService;

    @Override
    public void write(Chunk<? extends MomentTagContext> chunk) throws Exception {
        log.debug("🐛 [WRITER] 오늘의 일기 태그 저장");

        List<MomentTagContext> diaryTags = (List<MomentTagContext>) chunk.getItems();
        String date = diaryTags.get(0).getCreatedAt().substring(0, 10);

        try {
            tagStepService.insertAll(diaryTags, date);

        }catch (Exception e){
            log.error("💥 순간 일기 태그 저장 중 에러가 발생했습니다.", e);
            tagRedisService.pushAll(date, diaryTags);
            throw new RuntimeException(e);
        }
    }

}
