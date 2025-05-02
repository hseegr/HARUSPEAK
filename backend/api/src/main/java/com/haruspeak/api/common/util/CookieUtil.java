package com.haruspeak.api.common.util;

import com.haruspeak.api.common.exception.user.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

/**
 * 토큰으로 쿠키 생성
 */
public class CookieUtil {

    private CookieUtil() {}

    /**
     * 토큰 쿠키 생성
     * - accessToken / refreshToken 쿠키 공통 처리
     *
     * @param name 쿠키 이름
     * @param token JWT 토큰 문자열
     * @param expirationMs 만료 시간 (ms)
     * @return ResponseCookie
     */
    public static ResponseCookie createTokenCookie(String name, String token, long expirationMs) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationMs / 1000)  // ms → 초 변환
                .sameSite("Strict")
                .build();
    }

    /**
     * 쿠키에서 토큰 추출
     *
     * @param cookies 쿠키
     * @param token 토큰 accessToken / refreshToken
     */
    public static String extractTokenFromCookie(Cookie[] cookies, String token) {
        return Arrays.stream(cookies)
                .filter(cookie -> token.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    /**
     * 토큰 만료 처리
     * @param token 토큰 accessToken / refreshToken
     * @return ResponseCookie : 만료된 쿠키
     */
    public static ResponseCookie clearCookie(String token) {
        return ResponseCookie.from(token, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }
}
