package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.model.repository.MomentImageRepository;
import com.haruspeak.batch.service.TodayDiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayImageWriter implements ItemWriter<List<DailyMoment>> {

    private final MomentImageRepository momentImageRepository;


    @Override
    public void write(Chunk<? extends List<DailyMoment>> chunk) throws Exception {
        log.debug("🐛 STEP3.WRITE - 오늘의 일기 이미지 저장");

        List<DailyMoment> moments = new ArrayList<>();
        for (List<DailyMoment> momentList : chunk.getItems()) {
            moments.addAll(momentList);
        }

        try {
            momentImageRepository.bulkInsertMomentImages(moments);

        }catch (Exception e){
            log.error("⚠️ moment_images 삽입 중 에러가 발생했습니다.", e);
            throw e;
        }
    }

}

