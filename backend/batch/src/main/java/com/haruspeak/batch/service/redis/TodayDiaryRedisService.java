package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TodayDiaryRedisService {

    @Qualifier("batchDiaryRedisTemplate")
    private final RedisTemplate<String, TodayDiaryContext> redisTemplate;

    public TodayDiaryRedisService(@Qualifier("batchDiaryRedisTemplate")RedisTemplate<String, TodayDiaryContext> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyByDate(String date) {
        return String.format("step:todayDiary:date:%s", date);
    }

    public TodayDiaryContext popByDate(String date){
        return redisTemplate.opsForList().leftPop(getKeyByDate(date));
    }

    /**
     * TodaySummary를 진행해야 할 데이터 저장 (진행중 실패건)
     * @param diaries
     * @param date
     */
    public void pushAll(List<TodayDiaryContext> diaries, String date){
        try {
            redisTemplate.opsForList().rightPushAll(getKeyByDate(date), diaries);
        } catch (Exception e) {
            log.error("💥 TODAY SUMMARY STEP DATA REDIS 저장 실패 - date:{}", date, e);
            throw e;
        }
    }
}
