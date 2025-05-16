package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.MomentImageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ImageRedisService {

    @Qualifier("imageStepRedisTemplate")
    private final RedisTemplate<String, MomentImageContext> redisTemplate;

    public ImageRedisService(@Qualifier("imageStepRedisTemplate") RedisTemplate<String, MomentImageContext>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("step:image:date:%s",date);
    }

    public MomentImageContext popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * 썸네일 스텝 데이터 한번에 저장 FIFO 구조 사용
     * @param contexts
     * @param date
     */
    public void pushAll(List<MomentImageContext> contexts, String date){
        try {
            contexts.forEach(context -> {
                redisTemplate.opsForList().rightPush(getKeyByDate(date), context);
            });
        } catch (Exception e) {
            log.error("💥 이미지 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }

}
