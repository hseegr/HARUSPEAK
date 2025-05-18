package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ThumbnailRedisService {

    @Qualifier("thumbnailStepRedisTemplate")
    private final RedisTemplate<String, ThumbnailGenerateContext> redisTemplate;

    public ThumbnailRedisService(@Qualifier("thumbnailStepRedisTemplate") RedisTemplate<String, ThumbnailGenerateContext>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date){
        return String.format("step:thumbnail:date:%s",date);
    }

    public ThumbnailGenerateContext popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    public void pushAll(String date, List<ThumbnailGenerateContext> contexts){
        try {
            redisTemplate.opsForList().rightPushAll(
                    getKeyByDate(date), contexts
            );
        } catch (Exception e) {
            log.error("💥 썸네일 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }


}
