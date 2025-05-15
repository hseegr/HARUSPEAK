package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.model.MomentImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ImageRedisService {

    @Qualifier("imageStepRedisTemplate")
    private final RedisTemplate<String, List<MomentImage>> redisTemplate;

    public ImageRedisService(@Qualifier("imageStepRedisTemplate") RedisTemplate<String, List<MomentImage>>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("image:date:%s",date);
    }

    public List<MomentImage> popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * 썸네일 스텝 데이터 한번에 저장 FIFO 구조 사용
     * @param momentImages
     * @param date
     */
    public void pushAll(List<List<MomentImage>> momentImages, String date){
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), momentImages);
        } catch (Exception e) {
            log.error("💥 이미지 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }

}
