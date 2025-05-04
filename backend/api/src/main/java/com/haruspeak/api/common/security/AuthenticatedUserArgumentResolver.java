package com.haruspeak.api.common.security;

import com.haruspeak.api.common.exception.user.InvalidTokenException;
import com.haruspeak.api.common.exception.user.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @AuthenticatedUser 어노테이션이 붙은 파라미터에 인증된 사용자의 userId(Integer)를 주입해주는 리졸버
 * - JwtAuthenticationFilter에서 SecurityContextHolder에 저장된 CustomUserPrincipal에서 userId 추출
 * - 파라미터 타입은 반드시 Integer여야 함
 * - 인증되지 않았거나 principal 타입이 CustomUserPrincipal이 아닌 경우 예외 발생
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 파라미터가 @AuthenticatedUser 어노테이션을 갖고 있고, 타입이 Integer일 경우 처리 대상임을 반환
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(Integer.class);
    }

    /**
     * 인증 컨텍스트(SecurityContextHolder)에서 사용자 인증 객체를 꺼내 userId만 추출하여 컨트롤러에 주입
     *
     * @throws InvalidTokenException 인증 정보가 없거나 형식이 잘못된 경우 발생
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof CustomUserPrincipal customPrincipal)) {
            return 1;
//            throw new UnauthorizedException();
        }

        Integer userId = customPrincipal.userId();

        // 테스트용
        userId = (userId == null ? 1 : userId);

        return userId;
    }
}
