package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.model.TodayDiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class TodayDiaryRedisService {

    @Qualifier("batchDiaryRedisTemplate")
    private final RedisTemplate<String, TodayDiary> redisTemplate;

    public TodayDiaryRedisService(@Qualifier("batchDiaryRedisTemplate")RedisTemplate<String, TodayDiary> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return "todayDiary:date" + date;
    }

    public TodayDiary popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * TodaySummary를 진행해야 할 데이터 저장 (진행중 실패건)
     * @param diaries
     * @param date
     */
    public void pushAll(List<TodayDiary> diaries, String date){
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), diaries);
        } catch (Exception e) {
            log.error("💥 TODAY SUMAMRY STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}
