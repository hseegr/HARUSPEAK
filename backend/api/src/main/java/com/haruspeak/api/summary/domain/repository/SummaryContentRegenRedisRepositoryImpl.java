package com.haruspeak.api.summary.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

@RequiredArgsConstructor
public class SummaryContentRegenRedisRepositoryImpl implements SummaryContentRegenRepository {

    private final StringRedisTemplate redisTemplate;

    private String getKey(int userId, int summaryId) {
        return String.format("user:%s:summary:%s:regenerate", userId, summaryId);
    }

    @Override
    public void saveSummaryRegenState(int userId, int summaryId) {
        redisTemplate.opsForValue().set(getKey(userId, summaryId), "true");
    }

    @Override
    public boolean isSummaryGenerating(int userId, int summaryId) {
        return redisTemplate.hasKey(getKey(userId, summaryId));
    }

    @Override
    public void deleteSummaryRegenState(int userId, int summaryId) {
        redisTemplate.delete(getKey(userId, summaryId));
    }
}
