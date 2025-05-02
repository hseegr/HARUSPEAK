package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.security.JwtAuthenticationFilter;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.user.dto.CustomUserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.haruspeak.api.common.test.TestConstants.NAME;
import static com.haruspeak.api.common.test.TestConstants.USER_ID;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserController 테스트 완료
 *
 * 🧪 사용자 정보 요청:
 * - 성공
 */
@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = true)
@Import(UserControllerTest.TestJwtTokenConfig.class)
class UserControllerTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestJwtTokenConfig {
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return mock(JwtTokenProvider.class);
        }
    }


    @Nested
    @DisplayName("🧪 사용자 정보 조회 테스트")
    class GetCurrentUserTest {

        /**
         * [✅ 성공 테스트] 인증된 사용자 정보 조회
         *
         * 목적:
         * - CustomUserPrincipal이 주입될 때 userId, name이 올바르게 반환되는지 검증
         *
         * 입력:
         * - CustomUserPrincipal(userId=1, name="홍길동")
         *
         * 예상 결과:
         * - status 200 OK
         * - body: {"userId":1, "name":"홍길동"}
         */
        @Test
        @DisplayName("✅ 인증된 사용자 정보 조회 성공")
        void getCurrentUser_success() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(new CustomUserPrincipal(USER_ID, NAME), null, List.of());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // optional

            mockMvc.perform(get("/api/user/me")
                            .with(SecurityMockMvcRequestPostProcessors.authentication(authToken)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.name").value(NAME));
        }
    }
}
