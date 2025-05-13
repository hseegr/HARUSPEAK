package com.haruspeak.api.common.security;

import com.haruspeak.api.HaruspeakApiApplication;
import com.haruspeak.api.common.exception.user.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.haruspeak.api.common.test.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * JwtAuthenticationFilter 통합 테스트 완료
 *
 * 🧪 필터 통과 테스트:
 * - 성공 )
 * - 예외 ) accessToken 쿠키 없음
 *
 * 🧪 필터 예외 경로 테스트:
 * - 성공 ) shouldNotFilter 경로는 필터를 타지 않는다
 */
@SpringBootTest(classes = HaruspeakApiApplication.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("🧪 필터 통과 테스트")
    class JwtFilterTest {
        /**
         * [✅ 성공 테스트] 필터가 유효한 토큰을 검증하고 인증 객체를 주입
         * <p>
         * 목적:
         * - 필터가 accessToken 쿠키에서 JWT를 추출해 SecurityContext에 인증정보를 등록하는지 확인
         * <p>
         * 입력:
         * - userId: 사용자 ID
         * - name: 사용자 이름
         * <p>
         * 예상 결과:
         * - 인증 성공
         * - @AuthenticatedUser로부터 userId 추출
         */
        @Test
        @DisplayName("accessToken이 있으면 SecurityContext에 인증객체가 들어간다")
        void jwtFilter_validToken_authenticatesUser() throws Exception {
            String token = jwtTokenProvider.issueAccessToken(USER_ID, NAME);
            Cookie cookie = new Cookie(TOKEN_TYPE_ACCESS, token);

            mockMvc.perform(get(FILTER_TEST_API).cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(USER_ID)));
        }

        /**
         * [⚠️ 예외 테스트] 쿠키가 없으면 인증 실패
         * <p>
         * 목적:
         * - 필터가 쿠키가 없을 때 인증 객체를 주입하지 않아야 함
         * <p>
         * 입력:
         * - 쿠키 없음
         * <p>
         * 예상 결과:
         * - 401 Unauthorized + UNAUTHORIZED 응답
         */
        @Test
        @DisplayName("⚠️ accessToken 쿠키 없음 → UNAUTHORIZED (401)")
        void jwtFilter_noToken_returns401() throws Exception {
            assertThatThrownBy(() ->  mockMvc.perform(get(FILTER_TEST_API)))
                    .isInstanceOf(UnauthorizedException.class)
                    ;
        }
    }


    @Nested
    @DisplayName("🧪 필터 우회 테스트")
    class ShouldNotFilterPathTest {
        /**
         * [✅ 성공 테스트] 필터가 제외한 경로는 인증 없이 통과
         * <p>
         * 목적:
         * - shouldNotFilter()에서 제외된 경로(`/api/`)는 필터를 타지 않음
         * <p>
         * 입력:
         * - 경로: /api/auth/test/filter
         * <p>
         * 예상 결과:
         * - 필터 미작동 → 인증 없이 응답됨
         */
        @Test
        @DisplayName("✅ 필터 제외 경로 (/api/...)는 필터를 타지 않는다")
        void jwtFilter_shouldNotFilterPath_skipsFilter() throws Exception {
            mockMvc.perform(get("/api/auth/test/filter").contextPath(""))
                    .andExpect(status().isOk())
                    .andDo(print()); ;
        }
    }
}
