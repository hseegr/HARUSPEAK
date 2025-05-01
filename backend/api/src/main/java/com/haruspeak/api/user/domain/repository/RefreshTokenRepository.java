package com.haruspeak.api.user.domain.repository;

/**
 * 사용자별 RefreshToken 저장소 인터페이스
 * 저장 : Ridis
 *
 * - key: 사용자
 * - value: refreshToken
 */
public interface RefreshTokenRepository {

    /**
     * RefreshToken 저장
     * @param userId 사용자 ID
     * @param refreshToken 발급된 RefreshToken
     * @param expiration 유효 시간 (초 단위)
     */
    void saveRefreshToken(Integer userId, String refreshToken, long expiration);

    /**
     * RefreshToken 조회
     * @param userId 사용자 ID
     * @return 저장된 RefreshToken (없으면 null)
     */
    String getRefreshToken(Integer userId);

    /**
     * RefreshToken 삭제
     * @param userId 사용자 ID
     */
    void deleteRefreshToken(Integer userId);

}