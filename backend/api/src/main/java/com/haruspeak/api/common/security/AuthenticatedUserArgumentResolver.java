package com.haruspeak.api.common.security;

import com.haruspeak.api.common.exception.user.InvalidTokenException;
import com.haruspeak.api.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * SecurityContextHolder에서 인증 정보를 가져와 컨트롤러의 @AuthenticatedUser 파라미터에 주입합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookieUtil.extractTokenFromCookie(request, "accessToken");
        Integer userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            throw new InvalidTokenException();
        }
        return userId;
    }
}
