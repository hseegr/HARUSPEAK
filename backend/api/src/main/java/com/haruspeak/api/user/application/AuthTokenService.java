package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.user.InvalidTokenException;
import com.haruspeak.api.common.exception.user.UnauthorizedException;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.domain.repository.RefreshTokenRepositoryImpl;
import com.haruspeak.api.user.dto.TokenIssueResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepositoryImpl refreshTokenRepository;

    /**
     * 토큰 발급
     * @param userId 사용자 Id
     * @param name 사용자 이름
     * @return
     * - TokenIssueResult: 발급된 토큰
     */
    public TokenIssueResult issueToken(Integer userId, String name) {
        String accessToken = jwtTokenProvider.issueAccessToken(userId, name);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userId);

        ResponseCookie accessCookie = CookieUtil.createTokenCookie("accessToken", accessToken, jwtTokenProvider.getAccessTokenExpiration());
        ResponseCookie refreshCookie = CookieUtil.createTokenCookie("refreshToken", refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        // Redis에 리프레시 토큰 저장
        refreshTokenRepository.saveRefreshToken(userId, refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new TokenIssueResult(accessCookie, refreshCookie);
    }

    /**
     * 쿠키에 담긴 RefreshToken으로 AccessToken 재발급
     *
     * @param cookies 쿠키
     * @return
     * - TokenIssueResult : 발급된 토큰
     */
    public TokenIssueResult reissueTokens(Cookie[] cookies) {
        if(cookies == null) {
            throw new UnauthorizedException();
        }

        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = CookieUtil.extractTokenFromCookie(cookies, "refreshToken");
        if(refreshToken == null) {
            throw new UnauthorizedException();
        }

        jwtTokenProvider.validateTokenOrThrow(refreshToken);  // 유효성 검사

        Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String name = jwtTokenProvider.getNameFromToken(refreshToken);

        return issueToken(userId, name);
    }

    /**
     * 토큰 만료 처리 (로그아웃)
     * - filter 예외
     * @param cookies 쿠키
     * @return
     * - TokenIssueResult : 만료된 토큰
     */
    public TokenIssueResult expireToken(Cookie[] cookies) {
        if (cookies != null) {
            String refreshToken = CookieUtil.extractTokenFromCookie(cookies, "refreshToken");
            if (refreshToken != null) {
                Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
                if(userId != null) {
                    refreshTokenRepository.deleteRefreshToken(userId);
                }
            }
        }

        ResponseCookie expiredAccess = CookieUtil.clearCookie("accessToken");
        ResponseCookie expiredRefresh = CookieUtil.clearCookie("refreshToken");

        return new TokenIssueResult(expiredAccess, expiredRefresh);
    }

}
