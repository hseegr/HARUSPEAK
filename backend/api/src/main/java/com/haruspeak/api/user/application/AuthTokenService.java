package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.user.TokenSaveErrorException;
import com.haruspeak.api.common.exception.user.UnauthorizedException;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.domain.repository.RefreshTokenRedisRepository;
import com.haruspeak.api.user.dto.TokenIssueResult;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRepository;

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

        try {
            // Redis에 리프레시 토큰 저장
            refreshTokenRepository.saveRefreshToken(userId, refreshToken, jwtTokenProvider.getRefreshTokenExpiration());
            log.debug("✅ refreshToken 저장 완료 (userId: {})", userId);
        } catch (Exception e) {
            log.error("⛔ refreshToken 저장 실패 (userId: {}, token: {})", userId, refreshToken, e);
            throw new TokenSaveErrorException();
        }

        ResponseCookie accessCookie = CookieUtil.createTokenCookie("accessToken", accessToken, jwtTokenProvider.getAccessTokenExpiration());
        ResponseCookie refreshCookie = CookieUtil.createTokenCookie("refreshToken", refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

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
        String refreshToken = extractRefreshTokenOrThrow(cookies); // 쿠키에서 리프레시 토큰 추출
        jwtTokenProvider.validateTokenOrThrow(refreshToken);  // 유효성 검사

        Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String name = jwtTokenProvider.getNameFromToken(refreshToken);

        if(!refreshTokenRepository.getRefreshToken(userId).equals(refreshToken)) {
            log.debug("⛔ 리프레시 토큰 정보 불일치 (userId: {})", userId);
            throw new UnauthorizedException();
        }

        return issueToken(userId, name);
    }

    /**
     * 로그아웃
     * - filter 예외
     * @param cookies 쿠키
     * @return
     * - TokenIssueResult : 만료된 토큰⚠️
     */
    public TokenIssueResult logout(Cookie[] cookies) {
        try {
            String refreshToken = extractRefreshTokenOrThrow(cookies);
            Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            if(userId != null) {
                refreshTokenRepository.deleteRefreshToken(userId);
            }
        } catch (UnauthorizedException e) {
            log.debug("⚠️ 로그아웃 - 유효한 토큰 없음");
        } catch (Exception e) {
            log.warn("⚠️ refreshToken 삭제 실패", e);
        }

        return expireToken();
    }


    /**
     * 리프레시 토큰 추출
     * @param cookies 쿠키
     * @return 리프레시 토큰
     * @exception UnauthorizedException 쿠키: null OR refreshToken: null
     */
    private String extractRefreshTokenOrThrow(Cookie[] cookies) {
        if (cookies == null) {
            log.debug("⛔ 쿠키 없음");
            throw new UnauthorizedException();
        }

        String refreshToken = CookieUtil.extractTokenFromCookie(cookies, "refreshToken");
        if (refreshToken == null) {
            log.debug("⛔ refreshToken 없음");
            throw new UnauthorizedException();
        }

        return refreshToken;
    }


    /**
     * 토큰 만료
     * @return TokenIssueResult 만료처리된 토큰
     */
    private TokenIssueResult expireToken(){
        return new TokenIssueResult(
                CookieUtil.clearCookie("accessToken"),
                CookieUtil.clearCookie("refreshToken")
        );
    }

}
