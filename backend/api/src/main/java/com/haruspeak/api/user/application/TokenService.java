package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.user.UnauthorizedException;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.dto.TokenReissueResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * RefreshToken으로 AccessToken 재발급
     *
     * @param request HttpServletRequest
     * @return
     * - accessToken을 담은 ResponseEntity
     */
    @Transactional(readOnly = true)
    public TokenReissueResult reissueTokens(HttpServletRequest request) {
        String refreshToken = CookieUtil.extractTokenFromCookie(request, "refreshToken");
        Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String name = jwtTokenProvider.getNameFromToken(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId, name);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        ResponseCookie accessCookie = CookieUtil.createTokenCookie("accessToken", newAccessToken, jwtTokenProvider.getAccessTokenExpiration());
        ResponseCookie refreshCookie = CookieUtil.createTokenCookie("refreshToken", newRefreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new TokenReissueResult(accessCookie, refreshCookie);
    }
}
