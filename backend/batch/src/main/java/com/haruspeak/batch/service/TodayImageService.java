package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.MomentImageContext;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.service.redis.ImageRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayImageService {
    
    private final ImageRedisService redisService;
    
    public void saveImageStepData(List<TodayDiaryContext> diaries, String date){
        log.debug("🐛 이미지 STEP DATA REDIS 저장 실행");
        try {
            List<MomentImageContext> imageStepData = retrieveImageStepData(diaries);
            log.debug("IMAGE STEP DATA: {}건", imageStepData.size());
            redisService.pushAll(imageStepData, date);
        } catch (Exception e) {
            throw new RuntimeException("💥 아미지 STEP DATA REDIS 저장 실패", e);
        }
    }

    private List<MomentImageContext> retrieveImageStepData(List<TodayDiaryContext> diaries){
        return diaries.stream()
                .flatMap(diary -> getMomentImagesIfExists(diary).stream())
                .toList();
    }

    private List<MomentImageContext> getMomentImagesIfExists(TodayDiaryContext diary) {
        return diary.getDailyMoments().stream()
                .filter(moment -> moment.getImageCount() > 0)
                .map(moment -> new MomentImageContext(
                        moment.getUserId(),
                        moment.getMomentTime(),
                        moment.getImages()
                ))
                .toList();
    }

}
