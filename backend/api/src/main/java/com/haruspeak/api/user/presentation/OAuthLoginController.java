package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.application.TokenService;
import com.haruspeak.api.user.dto.TokenReissueResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "사용자 로그인 관련 API", description = "SNS 로그인 및 회원가입, 로그아웃, 토큰 재발급")
public class OAuthLoginController {

    @Value("${app.oauth2.google.authorization-uri}")
    private String authorizationUri;

    private final TokenService tokenService;

    /**
     * 구글 로그인 요청
     * @return
     * - HttpStatus 302 : FOUND
     * - 구글 로그인 URL로 리다이렉트
     */
    @GetMapping("/google/login")
    @Operation(
            summary = "구글 SNS 로그인",
            description
                    =   "구글 OAuth2 로그인 인증 과정을 시작합니다.\n" +
                        "요청 시 사용자는 구글 로그인 화면으로 리다이렉트되며, 로그인 완료 후 서버로 콜백됩니다."
    )
    public ResponseEntity<Void> redirectToGoogle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authorizationUri));
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }


    /**
     * RefreshToken을 통해 AccessToken 재발급
     *
     * @param request HttpServletRequest
     * @return token 쿠키 포함 응답
     */
    @PostMapping("/token/refresh")
    @Operation(summary = "Access Token 재발급", description = "쿠키에 저장된 refreshToken을 통해 accessToken을 재발급합니다.")
    public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request) {
        TokenReissueResult tokens = tokenService.reissueTokens(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokens.accessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshCookie().toString())
                .build();
    }


    /**
     * 로그아웃 요청
     *
     * @return 만료된 쿠키를 포함한 응답
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "클라이언트에 저장된 accessToken, refreshToken 쿠키를 삭제(만료)합니다.")
    public ResponseEntity<Void> logout() {
        ResponseCookie expiredAccess = CookieUtil.clearCookie("accessToken");
        ResponseCookie expiredRefresh = CookieUtil.clearCookie("refreshToken");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccess.toString())
                .header(HttpHeaders.SET_COOKIE, expiredRefresh.toString())
                .build();
    }



}
