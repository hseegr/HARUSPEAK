package com.haruspeak.api.user.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * 사용자별 RefreshToken 저장소
 * - key: "refreshToken:user:{userId}"
 * - value: refreshToken
 * - 만료 있음
*/
@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;

    /**
     * RefreshToken 저장
     * @param userId 사용자 ID
     * @param refreshToken 발급된 RefreshToken
     * @param expiration 유효 시간 (초 단위)
     */
    public void saveRefreshToken(Integer userId, String refreshToken, long expiration) {
        redisTemplate.opsForValue().set(getKey(userId), refreshToken, Duration.ofMillis(expiration));
    }

    /**
     * RefreshToken 조회
     * @param userId 사용자 ID
     * @return 저장된 RefreshToken (없으면 null)
     */
    public String getRefreshToken(Integer userId) {
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    /**
     * RefreshToken 삭제
     * @param userId 사용자 ID
     */
    public void deleteRefreshToken(Integer userId) {
        redisTemplate.delete(getKey(userId));
    }


    /**
     * 사용자별 refreashToken 저장 key
     * @param userId 사용자 ID
     * @return 사용자 key
     */
    private String getKey(Integer userId) {
        return "refreshToken:user:" + userId;
    }
}
