package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.application.AuthTokenService;
import com.haruspeak.api.user.dto.TokenIssueResult;
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
@Tag(
        name = "사용자 로그인 관련 API",
        description = "SNS 로그인 및 회원가입, 로그아웃, 토큰 재발급"
)
public class OAuthLoginController {

    @Value("${app.oauth2.google.authorization-uri}")
    private String authorizationUri;

    private final AuthTokenService tokenService;

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
     * @return token이 담긴 쿠키 포함 응답
     */
    @PostMapping("/token/refresh")
    @Operation(
            summary = "Access Token 재발급",
            description = "쿠키에 저장된 refreshToken을 통해 accessToken을 재발급합니다."
    )
    public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request) {
        TokenIssueResult tokens = tokenService.reissueTokens(request.getCookies());
        return buildTokenResponse(tokens);
    }


    /**
     * 로그아웃 요청
     * 
     * @param request HttpServletRequest
     * @return 만료된 토큰이 담긴 쿠키를 포함한 응답
     */
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃 요청",
            description = "클라이언트에 저장된 accessToken, refreshToken 쿠키를 삭제(만료)합니다."
    )
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        TokenIssueResult tokens = tokenService.expireToken(request.getCookies());
        return buildTokenResponse(tokens);
    }


    /**
     * 쿠키를 담아 응답을 생성하는 공통 메서드
     * @param tokens 발급된 토큰
     * @return 토큰을 담은 응답
     */
    private ResponseEntity<Void> buildTokenResponse(TokenIssueResult tokens) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokens.accessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshCookie().toString())
                .build();
    }
    

    @GetMapping("/test/filter")
    @Operation(
            summary = "filter 테스트",
            description = "필터 제외 테스트를 진행하기 위한 임시 api입니다."
    )
    public ResponseEntity<Void> filterTest() {
        return ResponseEntity.ok().build();
    }



}
