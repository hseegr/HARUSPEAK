package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.DailyMoment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TodayRedisRepository {

    @Qualifier("apiRedisTemplate")
    private final RedisTemplate<String, Object> apiRedisTemplate;

    public TodayRedisRepository(@Qualifier("apiRedisTemplate")RedisTemplate<String, Object> apiRedisTemplate) {
        this.apiRedisTemplate = apiRedisTemplate;
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

    public TodayDiary getTodayMomentsByKey(String key, String writeDate) {
        // `getUserId`, `getMomentsFromRedis`, `createDailySummary`, `createTodayDiary` 메서드를 활용하여 분리
        int userId = getUserId(key);
        List<DailyMoment> moments = getMomentsFromRedis(key);
        DailySummary summary = createDailySummary(userId, writeDate, moments.size());
        return createTodayDiary(summary, moments);
    }

    private List<DailyMoment> getMomentsFromRedis(String key) {
        Map<Object, Object> data = apiRedisTemplate.opsForHash().entries(key);
        return data.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    List<String> images = (List<String>) value.get("images");
                    List<String> tags = (List<String>) value.get("tags");
                    return createDailyMoment(entry, value, images, tags);
                })
                .toList();
    }

    private DailyMoment createDailyMoment(Map.Entry<Object, Object> entry, Map<String, Object> value,
                                          List<String> images, List<String> tags) {
        return DailyMoment.builder()
                .createdAt(entry.getKey().toString())
                .momentTime(value.get("momentTime").toString())
                .content(value.get("content").toString())
                .images(images)
                .tags(tags)
                .imageCount(images.size())
                .tagCount(tags.size())
                .build();
    }

    private DailySummary createDailySummary(int userId, String writeDate, int momentCount) {
        return new DailySummary(userId, writeDate, momentCount);
    }

    private TodayDiary createTodayDiary(DailySummary dailySummary, List<DailyMoment> moments) {
        return new TodayDiary(dailySummary, moments);
    }


    public void delete(Integer userId, String date) {
        apiRedisTemplate.opsForHash().delete(getKey(String.valueOf(userId), date));
    }




}
