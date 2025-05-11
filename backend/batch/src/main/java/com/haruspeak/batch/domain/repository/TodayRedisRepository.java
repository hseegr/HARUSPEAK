package com.haruspeak.batch.domain.repository;

import com.haruspeak.batch.domain.TodayMoment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TodayRedisRepository {

    private final RedisTemplate<String, Object> apiRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public TodayRedisRepository(@Qualifier("apiRedisTemplate")RedisTemplate<String, Object> apiRedisTemplate,
                                @Qualifier("redisTemplate")RedisTemplate<String, Object> redisTemplate
    ) {
        this.apiRedisTemplate = apiRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    public int getUserId(String key) {
        return Integer.parseInt(key.split(":")[1]);
    }

    public String getAllUserKey(String date) {
        return getKey("*", date);
    }

    private String getKey(String userId, String date) {
        return "user:" + userId + ":" + date;
    }

    public Set<String> getAllKeys (String date) {
        return apiRedisTemplate.keys("user:*:moment:" + date);
    }

    public Map<Integer, List<TodayMoment>> getTodayMomentsByKey(String key) {
        int userId = getUserId(key);
        Map<Object, Object> data = apiRedisTemplate.opsForHash().entries(key);
        return new HashMap<Integer, List<TodayMoment>>(){{
            put(userId, data.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        return new TodayMoment(
                                entry.getKey().toString(),
                                value.get("momentTime").toString(),
                                value.get("content").toString(),
                                (List<String>)value.get("images"),
                                (List<String>)value.get("tags")
                        );
                    })
                    .toList());
        }};
    }

    public void delete(Integer userId, String date) {
        apiRedisTemplate.opsForHash().delete(getKey(String.valueOf(userId), date));
    }




}
