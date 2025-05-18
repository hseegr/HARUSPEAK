package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.MomentTagContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TagRedisService {

    @Qualifier("tagStepRedisTemplate")
    private final RedisTemplate<String, MomentTagContext> redisTemplate;

    public TagRedisService(@Qualifier("tagStepRedisTemplate") RedisTemplate<String, MomentTagContext>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("step:tag:date:%s",date);
    }

    public MomentTagContext popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    public void pushAll(String date, List<MomentTagContext> contexts){
        try {
            redisTemplate.opsForList().rightPushAll(
                    getKeyByDate(date),
                    contexts.toArray(new MomentTagContext[0])
            );
        } catch (Exception e) {
            log.error("💥 태그 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}
