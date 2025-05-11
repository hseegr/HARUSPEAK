package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.SummaryThumnailRegenState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SummaryThumnailRegenStateRedisRepositoryImpl implements SummaryThumnailRegenStateRedisRepository {

    private final RedisTemplate<String, SummaryThumnailRegenState> redisTemplate;

    @Override
    public void save(String userId, String summaryId, SummaryThumnailRegenState state) {

    }

    @Override
    public void delete(String userId, String summaryId) {

    }

    @Override
    public SummaryThumnailRegenState findBySummaryId(int userId, int summaryId) {
        Object value =  redisTemplate.opsForHash().get(getRedisKey(userId), summaryId);
        return value == null ? null : (SummaryThumnailRegenState) value;
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


    public String getRedisKey(int userId) {
        return "user:" + userId + ":image:regeneration";
    }
}
