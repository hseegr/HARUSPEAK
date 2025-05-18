package com.haruspeak.batch.service.redis;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TodayMomentRedisService {

    @Qualifier("apiRedisTemplate")
    private final RedisTemplate<String, Object> apiRedisTemplate;

    public TodayMomentRedisService(@Qualifier("apiRedisTemplate") RedisTemplate<String, Object> apiRedisTemplate) {
        this.apiRedisTemplate = apiRedisTemplate;
    }

    private int getUserId(String key) {
        return Integer.parseInt(key.split(":")[1]);
    }

    public String getKey(String userId, String date) {
        return "user:" + userId + ":moment:" + date;
    }

    public Set<String> getAllKeys(String date) {
        return apiRedisTemplate.keys(getKey("*", date));
    }

    public TodayDiaryContext getTodayMomentsByKey(String key, String date) {
        int userId = getUserId(key);
        List<DailyMoment> moments = getMomentsFromRedis(key, userId);
        DailySummary summary = createDailySummary(userId, date, moments.size());

        log.debug("🔍 userId:{}, summary:{}", userId, summary);
        return createTodayDiary(summary, moments);
    }

    public TodayDiaryContext getTodayMomentsByUserAndDate(String userId, String date) {
        String key = getKey(userId, date);
        return getTodayMomentsByKey(key, date);
    }

    private List<DailyMoment> getMomentsFromRedis(String key, int userId) {
        Map<Object, Object> data = apiRedisTemplate.opsForHash().entries(key);

        return data.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    Set<String> images = new HashSet<>((List<String>) value.get("images"));
                    Set<String> tags = new HashSet<>((List<String>) value.get("tags"));
                    return createDailyMoment(entry, value, images, tags, userId);
                })
                .sorted(Comparator.comparing(DailyMoment::getMomentTime))
                .toList();
    }

    private DailyMoment createDailyMoment(Map.Entry<Object, Object> entry, Map<String, Object> value,
                                          Set<String> images, Set<String> tags, int userId) {
        return DailyMoment.builder()
                .userId(userId)
                .createdAt(entry.getKey().toString().substring(0, 19))
                .momentTime(value.get("momentTime").toString().substring(0, 19)) // yyyy-MM-dd'T'HH:mm:ss
                .content(value.get("content").toString())
                .images(images)
                .tags(tags)
                .build();
    }

    private DailySummary createDailySummary(int userId, String writeDate, int momentCount) {
        return new DailySummary(userId, writeDate, momentCount);
    }

    private TodayDiaryContext createTodayDiary(DailySummary dailySummary, List<DailyMoment> moments) {
        return new TodayDiaryContext(dailySummary, moments);
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
