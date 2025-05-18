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

    public ImageRedisService(@Qualifier("imageStepRedisTemplate") RedisTemplate<String, MomentImageContext> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("step:image:date:%s", date);
    }

    public MomentImageContext popByDate(String date) {
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    public void pushAll(String date, List<MomentImageContext> contexts) {
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), contexts.toArray(new MomentImageContext[0]));
        } catch (Exception e) {
            log.error("💥 이미지 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}