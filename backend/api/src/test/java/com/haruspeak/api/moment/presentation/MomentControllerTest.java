package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.moment.application.ActiveDailyMomentService;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MomentController.class)
@AutoConfigureMockMvc(addFilters = false)
class MomentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActiveDailyMomentService activeDailyMomentService;

    @Test
    @DisplayName("성공: 인증된 사용자가 momentId로 조회")
    void getMomentDetail_success() throws Exception {
        // given
        Integer momentId = 1;
        Integer userId = 10; // 가짜 userId
        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null);

        MomentDetailResponse fakeResponse = new MomentDetailResponse(
                momentId,
                LocalDateTime.now(),
                List.of("https://image1.com", "https://image2.com"),
                "오늘 하루 내용",
                List.of("태그1", "태그2")
        );

        when(activeDailyMomentService.findMomentDetail(momentId))
                .thenReturn(fakeResponse);

        // when & then
        mockMvc.perform(get("/moment/{momentId}", momentId)
                        .principal(auth)) // 인증 정보 세팅
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.momentId").value(momentId))
                .andExpect(jsonPath("$.content").value("오늘 하루 내용"))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 인증 정보 없을 때 USER_NOT_FOUND 예외")
    void getMomentDetail_authenticationNull_fail() throws Exception {
        // given
        Integer momentId = 1;

        // when & then
        mockMvc.perform(get("/moment/{momentId}", momentId)) // 인증 없음
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
