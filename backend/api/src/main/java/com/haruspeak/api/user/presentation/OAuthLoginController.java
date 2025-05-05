package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.user.application.AuthTokenService;
import com.haruspeak.api.user.dto.TokenIssueResult;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth",
        description = "사용자 로그인 관련 API"
)
// filter 제외
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
        log.info("[GET] api/auth/google/login 로그인 요청");
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
            description = "쿠키에 저장된 refreshToken을 통해 accessToken을 재발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "401", 
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request) {
        log.info("[POST] api/auth/token/refresh 토큰 재발급 요청");
        return buildTokenResponse(tokenService.reissueTokens(request.getCookies()));
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
            description = "accessToken, refreshToken 쿠키를 삭제(만료)합니다."
    )
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        log.info("[POST] api/auth/logout 로그아웃 요청");
        return buildTokenResponse(tokenService.logout(request.getCookies()));
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


    /**
     * 필터 제외 테스트를 진행하기 위한 임시 api
     */
    @Hidden
    @GetMapping("/test/filter")
    public ResponseEntity<Void> filterTest() {
        log.info("🧪[GET] api/auth/test/filter FILTER TEST");
        return ResponseEntity.ok().build();
    }



}
