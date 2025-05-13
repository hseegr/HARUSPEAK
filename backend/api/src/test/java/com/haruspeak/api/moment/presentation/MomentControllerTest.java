package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.HaruspeakApiApplication;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.moment.application.MomentService;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.haruspeak.api.common.test.TestConstants.NAME;
import static com.haruspeak.api.common.test.TestConstants.USER_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MomentController.class)
@AutoConfigureMockMvc(addFilters = true)
@SpringBootTest(classes = HaruspeakApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MomentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private MomentService activeDailyMomentService;

    @Test
    @DisplayName("성공: 인증된 사용자가 momentId로 조회")
    void getMomentDetail_success() throws Exception {

        String accessToken = jwtTokenProvider.issueAccessToken(USER_ID, NAME);

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        Integer momentId = 1;

        MomentDetailResponse fakeResponse = new MomentDetailResponse(
                1,
                momentId,
                LocalDateTime.now(),
                2,
                List.of("https://image1.com", "https://image2.com"),
                "오늘 하루 내용",
                2,
                List.of("태그1", "태그2")
        );

        when(activeDailyMomentService.getMomentDetailByMomentId(USER_ID, momentId))
                .thenReturn(fakeResponse);

        // when & then
        mockMvc.perform(get("/api/moment/{momentId}", momentId).cookie(cookie)) // 인증 정보 세팅
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.momentId").value(momentId))
                .andExpect(jsonPath("$.content").value("오늘 하루 내용"))
                .andDo(print());
    }
}
