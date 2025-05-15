package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import com.haruspeak.batch.model.TodayDiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class TodayRedisService {

    @Qualifier("apiRedisTemplate")
    private final RedisTemplate<String, Object> apiRedisTemplate;

    public TodayRedisService(@Qualifier("apiRedisTemplate") RedisTemplate<String, Object> apiRedisTemplate) {
        this.apiRedisTemplate = apiRedisTemplate;
    }

    public int getUserId(String key) {
        return Integer.parseInt(key.split(":")[1]);
    }

    public String getKey(String userId, String date) {
        return "user:" + userId + ":moment:" + date;
    }

    public Set<String> getAllKeys(String date) {
        return apiRedisTemplate.keys(getKey("*", date));
    }

    public TodayDiary getTodayMomentsByKey(String key, String writeDate) {
        int userId = getUserId(key);
        List<DailyMoment> moments = getMomentsFromRedis(key, userId);
        DailySummary summary = createDailySummary(userId, writeDate, moments.size());

        log.debug("🔍 userId:{}, summary:{}", userId, summary);
        return createTodayDiary(summary, moments);
    }

    private List<DailyMoment> getMomentsFromRedis(String key, int userId) {
        Map<Object, Object> data = apiRedisTemplate.opsForHash().entries(key);

        return data.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    List<String> images = (List<String>) value.get("images");
                    List<String> tags = (List<String>) value.get("tags");
                    return createDailyMoment(entry, value, images, tags, userId);
                })
                .sorted(Comparator.comparing(DailyMoment::getMomentTime))
                .toList();
    }

    private DailyMoment createDailyMoment(Map.Entry<Object, Object> entry, Map<String, Object> value,
                                          List<String> images, List<String> tags, int userId) {
        return DailyMoment.builder()
                .userId(userId)
                .createdAt(entry.getKey().toString())
                .momentTime(value.get("momentTime").toString().substring(0, 19)) // yyyy-MM-dd'T'HH:mm:ss
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

    public void delete(String userId, String date) {
        try {
            apiRedisTemplate.delete(getKey(userId, date));
        } catch (Exception e) {
            log.error("💥 [SUMMARY STEP] 완료된 하루 일기 API REDIS 삭제 실패 - userId:{}, date:{}", userId, date, e);
            throw e;
        }
    }
}
