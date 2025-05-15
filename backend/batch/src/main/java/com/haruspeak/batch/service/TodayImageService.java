package com.haruspeak.batch.service;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.MomentImage;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.service.redis.ImageRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayImageService {
    
    private final ImageRedisService redisService;
    
    public void saveImageStepData(List<TodayDiary> diaries, String date){
        log.debug("🐛 이미지 STEP DATA REDIS 저장 실행");
        try {
            List<List<MomentImage>> imageStepData = retrieveImageStepData(diaries);
            redisService.pushAll(imageStepData, date);
        } catch (Exception e) {
            throw new RuntimeException("💥 아미지 STEP DATA REDIS 저장 실패", e);
        }
    }

    private List<List<MomentImage>> retrieveImageStepData(List<TodayDiary> diaries){
        return diaries.stream()
                .map(this::getMomentImagesIfExists)
                .filter(list -> !list.isEmpty())
                .toList();
    }

    private List<MomentImage> getMomentImagesIfExists(TodayDiary diary) {
        return diary.getDailyMoments().stream()
                .filter(moment -> moment.getImageCount() > 0)
                .map(moment -> new MomentImage(
                        moment.getUserId(),
                        moment.getMomentTime(),
                        moment.getImages()
                ))
                .toList();
    }

}
