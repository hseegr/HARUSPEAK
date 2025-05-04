package com.haruspeak.api.common.security;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.user.InvalidJwtInputException;
import com.haruspeak.api.common.exception.user.InvalidTokenException;
import com.haruspeak.api.user.dto.CustomUserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰 발급기
 * - AccessToken : 사용자 인증용
 * - RefreshToken : 토큰 재발급용
 */
@Component
@RequiredArgsConstructor
@Setter
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.expiration-ms.access}")
    private long accessTokenExpiration;

    @Getter
    @Value("${jwt.expiration-ms.refresh}")
    private long refreshTokenExpiration;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Getter
    private Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Access Token 생성
     *
     * @param userId 사용자 ID (PK)
     * @param name 사용자 이름
     * @return JWT 문자열
     */
    public String issueAccessToken(Integer userId, String name) {
        if (userId == null) {
            log.debug("⛔ Access 토큰 발급 실패 - userId 없음");
            throw new InvalidJwtInputException("userId");
        }

        if (name == null || name.isBlank()) {
            log.debug("⛔ Access 토큰 발급 실패 - name 없음");
            throw new InvalidJwtInputException("name");
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("name", name)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param userId 사용자 ID (PK)
     * @return JWT 문자열
     */
    public String issueRefreshToken(Integer userId) {
        if (userId == null) {
            log.debug("⛔ Refresh 토큰 발급 실패 - userId 없음");
            throw new InvalidJwtInputException("userId");
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Authorization 헤더 - 토큰 포맷 생성
     *
     * @param token JWT 문자열
     * @return "Bearer {token}" 형태의 문자열
     */
    public String getTokenWithPrefix(String token) {
        return tokenPrefix + token;
    }

    /**
     * 토큰에서 userId 추출
     *
     * @param token JWT 문자열
     * @return userId (Integer)
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Integer.class);
    }

    /**
     * 토큰에서 name 추출
     *
     * @param token JWT 문자열
     * @return 사용자 이름 (String)
     */
    public String getNameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("name", String.class);
    }

    /**
     * JWT 토큰 문자열을 파싱하여 Claims 추출
     *
     * @param token JWT 문자열
     * @return Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 유효성 검사
     * - 만료 여부 / 서명 위조 여부 / 구조 손상 여부 검사
     *
     * @param token JWT 문자열
     * 유효하면 성공, 아니면 예외
     */
    public void validateTokenOrThrow(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            log.debug("⛔ 인증 실패 - 토큰 만료 - exp: {}", e.getClaims().getExpiration());
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            log.debug("⛔ 인증 실패 - 잘못된 토큰 형식 - token: {}", token);
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN_FORMAT);
        } catch (Exception e) {
            log.debug("⛔ 인증 실패 - 토큰 유효성 검사 중 에러 발생 - error: {}", e.getMessage());
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * JWT로부터 Authentication 객체 생성
     * - Principal: CustomUserPrincipal(userId, name)
     * - Credentials: null (패스워드 없음)
     * - Authorities: 권한 없음
     *
     * @param token JWT accessToken
     * @return 인증 객체 (UsernamePasswordAuthenticationToken)
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        String name = claims.get("name", String.class);

        CustomUserPrincipal principal = new CustomUserPrincipal(userId, name);
        log.debug("✅ 인증 객체 생성(userId: {}, name: {})", userId, name);
        return new UsernamePasswordAuthenticationToken(principal, null, List.of());
    }


}
