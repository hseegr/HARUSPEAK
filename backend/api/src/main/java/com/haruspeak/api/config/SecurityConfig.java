package com.haruspeak.api.config;

import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.common.util.CookieUtil;
import com.haruspeak.api.user.application.AuthTokenService;
import com.haruspeak.api.user.application.OAuthLoginService;
import com.haruspeak.api.user.domain.User;
import com.haruspeak.api.user.dto.TokenIssueResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.Map;

/**
 * Spring Security 설정
 * - OAuth2 구글 로그인 성공 시 사용자 인증 처리 + JWT 응답 처리
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final OAuthLoginService oAuthLoginService;
    private final AuthTokenService authTokenService;

    @Value("${app.oauth2.redirect-uri}")
    private String frontendRedirectUri;

    /**
     * 시큐리티 필터 체인 설정
     * - 모든 요청 허용 (테스트 단계)
     * - 모든 요청 비허용 (테스트 종료 후 - 운영)
     * - OAuth2 로그인 성공 시 JWT 생성 및 리다이렉트 처리
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("🐛 시큐리티 필터 체인");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                // 허용할 요청
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/auth/google/**", "/oauth2/**").permitAll()
                        .anyRequest().permitAll() //
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(this::handleOAuthSuccess)
                );

        return http.build();
    }

    /**
     * OAuth2 로그인 성공 시 실행
     * - 구글 사용자 정보(sub, email, name) 파싱
     * - 로그인 처리 / JWT 생성
     * - JWT를 헤더에 담아 응답 / 리다이렉트
     */
    private void handleOAuthSuccess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        // 구글에게 받아온 정보
        String sub = String.valueOf(attributes.get("sub"));
        String email = String.valueOf(attributes.get("email"));
        String name = String.valueOf(attributes.get("name"));
        log.debug("✅ 구글 로그인 성공(subject: {}, email: {}, name: {}) ", sub, email, name);

        // 로그인 / 회원가입 후 로그인 진행
        User user = oAuthLoginService.processLoginOrRegister(sub, email, name);
        log.debug("로그인 사용자: {}", user);

        // 토큰 발급 - DB에 저장된 이름으로 토큰에 저장
        TokenIssueResult token = authTokenService.issueToken(user.getUserId(), user.getName());

        response.addHeader(HttpHeaders.SET_COOKIE, token.accessCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, token.refreshCookie().toString());

        response.sendRedirect(frontendRedirectUri); // 프론트 메인으로 이동
    }
}
