package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.TodayDiary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class TodayDiaryRedisRepository {

    @Qualifier("batchDiaryRedisTemplate")
    private final RedisTemplate<String, TodayDiary> redisTemplate;

    public TodayDiaryRedisRepository(@Qualifier("batchDiaryRedisTemplate")RedisTemplate<String, TodayDiary> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveTodayDiaryToRedis(String userId, String date, TodayDiary todayDiary) {
        redisTemplate.opsForValue().set(getKey(userId, date), todayDiary);
    }

    public TodayDiary getTodayDiaryFromRedis(String userId, String date) {
        return redisTemplate.opsForValue().get(getKey(userId, date));
    }

    public Set<String> getAllKeys (String date) {
        return redisTemplate.keys(getKey("*", date));
    }

    public TodayDiary getTodayDiaryByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    private String getKey(String userId, String date) {
        return "user:" + userId + "diary:" + date;
    }

    public int getUserId(String key) {
        return Integer.parseInt(key.split(":")[1]);
    }
}
