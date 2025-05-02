package com.haruspeak.api.common.security;

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
import java.util.List;

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

    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/oauth2/**",
            "/login/oauth2/**",
            "/api/auth/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("필터 진입 : {}", request.getRequestURI());
        // request에서 쿠키 추출
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            throw new UnauthorizedException();
        }

        // accessToken 쿠키에서 토큰 추출
        String token = CookieUtil.extractTokenFromCookie(request.getCookies(), "accessToken");
        log.debug("accessToken: {}", token);
        if(token == null) {
            throw new UnauthorizedException();
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
     * @param request
     * @return
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("필터 체크 중 요청 경로: {}", path); // 여기서도 경로를 확인하여 필터가 적용되기 전 체크

        // 경로 매칭 확인
        boolean shouldFilter = EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));  // 패턴과 경로를 매칭

        log.info("필터 적용 여부: {}", !shouldFilter); // 필터가 적용될지 여부를 확인

        return shouldFilter;
    }
}

