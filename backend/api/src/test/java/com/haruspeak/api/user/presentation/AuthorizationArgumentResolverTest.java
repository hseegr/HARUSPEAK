package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.security.AuthenticatedUserArgumentResolver;
import com.haruspeak.api.common.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @AuthenticatedUser 어노테이션 테스트 완료
 *
 * 🧪 어노테이션으로 userId 뽑기
 * - 성공
 *
 */
@WebMvcTest(AuthorizationTestController.class)
@Import(AuthorizationArgumentResolverTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorizationArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /// 🧪🧪🧪 어노테이션으로 userId 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] @Authorization Integer userId
     *
     * 목적:
     * - @Authorization 을 사용했을 때 userId가 추출되는지 검증
     *
     * 입력:
     * - userId: 1234
     * - name: "이름"
     *
     * 예상 결과:
     * - HttpStatus == Ok(200)
     * - content == "1234"
     */
    @Test
    void authorizationResolver_success() throws Exception {
        String token = jwtTokenProvider.createAccessToken(1234, "이름");

        mockMvc.perform(get("/test/athenticatedUser")
                        .cookie(new Cookie("accessToken", token)))
                .andExpect(status().isOk())
                .andExpect(content().string("1234"));
    }

    // ✅ 테스트 전용 Config
    static class TestConfig {
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            JwtTokenProvider provider = new JwtTokenProvider();
            provider.setSecretKey("12345678901234567890123456789012"); // 32byte 이상
            provider.setAccessTokenExpiration(3600000); // 1시간
            provider.setRefreshTokenExpiration(3600000); // 1시간
            provider.setTokenPrefix("Bearer ");
            provider.init(); // 꼭 초기화
            return provider;
        }

        @Bean
        public AuthenticatedUserArgumentResolver authorizationArgumentResolver(JwtTokenProvider jwtTokenProvider) {
            return new AuthenticatedUserArgumentResolver(jwtTokenProvider);
        }
    }
}
