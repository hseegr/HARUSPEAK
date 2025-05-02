package com.haruspeak.api.common.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * JWT 토큰에서 추출한 userId를 Controller에서 @AuthenticatedUser으로 주입 받기 위한 어노테이션
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented // 문서 표기
@AuthenticationPrincipal
public @interface AuthenticatedUser {
}
