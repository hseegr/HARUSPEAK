package com.haruspeak.api.config;

import com.haruspeak.api.common.security.AuthenticatedUserArgumentResolver;
import com.haruspeak.api.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * API 호출 허용 경로 설정
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://k12a607.p.ssafy.io",
                        "https://haruspeak.com"
                ) // react
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // method
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true); // 쿠키 허용
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticatedUserArgumentResolver(jwtTokenProvider));
    }

}

