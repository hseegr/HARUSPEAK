package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.TodayDiaryTagContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagRedisService {

    @Qualifier("tagStepRedisTemplate")
    private final RedisTemplate<String, TodayDiaryTagContext> redisTemplate;

    public TagRedisService(@Qualifier("tagStepRedisTemplate") RedisTemplate<String, TodayDiaryTagContext>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("step:tag:date:%s",date);
    }

    public TodayDiaryTagContext popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * 썸네일 스텝 데이터 한번에 저장 FIFO 구조 사용
     * @param todayDiaryTags
     * @param date
     */
    public void pushAll(List<TodayDiaryTagContext> todayDiaryTags, String date){
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), todayDiaryTags);
        } catch (Exception e) {
            log.error("💥 태그 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}
