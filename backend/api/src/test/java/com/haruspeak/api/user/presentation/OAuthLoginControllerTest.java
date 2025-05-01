package com.haruspeak.api.user.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * OAuthLoginControllerTest 테스트 완료
 *
 * 🧪 구글 로그인 요청:
 * - 성공
 */
@WebMvcTest(OAuthLoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "app.oauth2.google.authorization-uri=/oauth2/authorization/google"
})
class OAuthLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /// 🧪🧪🧪 구글 로그인 요청 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] 구글 로그인 요청 → OAuth2 인증 리다이렉트
     *
     * 목적:
     * - 클라이언트가 `/api/auth/google/login` 호출 시, OAuth2 인증 페이지로 정상 리다이렉트 되는지 확인
     *
     * 입력:
     * - HTTP Method: POST
     * - Authorization 헤더 없음 (비인증 상태)
     *
     * 예상 결과:
     * - HTTP 응답 코드: 302 (FOUND)
     * - 응답 Location 헤더: "/oauth2/authorization/google"
     *
     * ⚠️ 주의:
     * - Spring Security Filter 비활성화 후 테스트 (@AutoConfigureMockMvc(addFilters = false))
     */
    @Test
    @DisplayName("✅ 구글 로그인 요청 시 302 Redirect 응답과 Location 헤더를 반환")
    void redirectToGoogleTest() throws Exception {
        mockMvc.perform(get("/api/auth/google/login"))
                .andExpect(status().isFound()) // 302 FOUND
                .andExpect(header().string("Location", "/oauth2/authorization/google"));
    }



}
