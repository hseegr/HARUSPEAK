package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.SummaryThumbnailRegenState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SummaryThumnailRegenStateRedisRepositoryImpl implements SummaryThumnailRegenStateRedisRepository {

    private final RedisTemplate<String, Map<String, SummaryThumbnailRegenState>> thumbnailRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public Object findBySummaryId(int userId, int summaryId) {
        return redisTemplate.opsForHash().get(getRedisKey(userId), String.valueOf(summaryId));
    }

    /**
     * 임시 - 썸네일 재생성 상태열에 있는지만 확인 (fail, success 확인하지 않음)
     * @param userId
     * @param summaryId
     * @return
     */
    @Override
    public boolean isGenereatingOfSummary(int userId, int summaryId) {
        return findBySummaryId(userId, summaryId) != null;
    }


    private String getRedisKey(int userId) {
        return "user:" + userId + ":image:regeneration";
    }
}
