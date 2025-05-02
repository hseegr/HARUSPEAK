package com.haruspeak.api.user.presentation;

import com.haruspeak.api.HaruspeakApiApplication;
import com.haruspeak.api.common.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = HaruspeakApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // 필터 포함
class AuthorizationArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /// 🧪🧪🧪 어노테이션으로 userId 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] @AuthenticatedUser Integer userId
     *
     * 목적:
     * - @AuthenticatedUser 을 사용했을 때 userId가 추출되는지 검증
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
    @DisplayName("✅ @AuthenticatedUser 사용 시 필터 적용 후 userId를 반환")
    void integration_filterToController_authenticatedUserInjection() throws Exception {
        // given
        int userId = 1234;
        String name = "이름";
        String accessToken = jwtTokenProvider.issueAccessToken(userId, name);

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // when + then
        mockMvc.perform(get("/test/authenticatedUser").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(userId))); // "1234"
    }
}
