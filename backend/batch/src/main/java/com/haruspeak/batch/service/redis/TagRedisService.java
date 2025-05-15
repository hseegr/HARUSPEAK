package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.model.TodayDiaryTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagRedisService {

    @Qualifier("tagStepRedisTemplate")
    private final RedisTemplate<String, TodayDiaryTag> redisTemplate;

    public TagRedisService(@Qualifier("tagStepRedisTemplate") RedisTemplate<String, TodayDiaryTag>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("tag:date:%s",date);
    }

    public TodayDiaryTag popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * 썸네일 스텝 데이터 한번에 저장 FIFO 구조 사용
     * @param todayDiaryTags
     * @param date
     */
    public void pushAll(List<TodayDiaryTag> todayDiaryTags, String date){
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), todayDiaryTags);
        } catch (Exception e) {
            log.error("💥 태그 STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}
