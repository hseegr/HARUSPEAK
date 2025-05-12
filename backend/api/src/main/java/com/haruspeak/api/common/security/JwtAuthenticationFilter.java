package com.haruspeak.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.user.UnauthorizedException;
import com.haruspeak.api.common.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT 인증 필터
 * - 요청마다 accessToken 쿠키에서 JWT 추출
 * - 토큰이 유효하면 SecurityContext 에 Authentication 등록
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> TEST_PATTERNS = List.of(
            "/api/moment/**",
            "/api/today/**",
            "/api/summary/**",
            "/api/main",
            "/api/user/tags"
    );

    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/oauth2/**",
            "/login/oauth2/**",
            "/auth/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.debug("필터 진입 요청 경로 : {}", path);

        // request에서 쿠키 추출
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            log.debug("⛔ 인증 불가 - 쿠키 없음 (요청 경로: {})", path);
            handleUnauthorized(response);
            return;
        }

        // accessToken 쿠키에서 토큰 추출
        String token = CookieUtil.extractTokenFromCookie(request.getCookies(), "accessToken");
        if(token == null) {
            log.debug("⛔ 인증 불가 - accessToken 없음 (요청 경로: {})", path);
            handleUnauthorized(response);
            return;
        }

        // 유효성 검사 후 인증 객체 생성
        jwtTokenProvider.validateTokenOrThrow(token);
        Authentication auth = jwtTokenProvider.getAuthentication(token);

        // SecurityContext에 인증 정보 저장
        if (auth instanceof UsernamePasswordAuthenticationToken authentication) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }


    /**
     * 로그아웃 로그인, 토큰 재발급 필터 제외
     * @param request 요청
     * @return 필터 제외 여부
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 경로 매칭 확인
        boolean isExcluded = EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));  // 패턴과 경로를 매칭

        log.debug("🔍필터 적용: {}, 요청 경로: {}", !isExcluded, path); // 필터가 적용될지 여부를 확인 : true:적용 / false :미적용
        
//        if(!isExcluded) { // 미적용일 때, TEST ROOT 는 빠져나가게 함
//            boolean isTestExcluded = TEST_PATTERNS.stream()
//                    .anyMatch(pattern -> pathMatcher.match(pattern, path));
//            log.debug("⌛ 테스트 제외 경로 여부: {}", isTestExcluded);
//            isExcluded = isTestExcluded;
//        }

        return isExcluded;
    }

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        log.debug("⛔ 필터 통과 실패");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ErrorCode error = ErrorCode.UNAUTHORIZED;
        Map<String, Object> body = new HashMap<>();
        body.put("code", error.getCode());
        body.put("message", error.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("details", null);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}

