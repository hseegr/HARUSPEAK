package com.haruspeak.batch.common.client.fastapi;

import com.haruspeak.batch.common.client.ApiClientHelper;
import com.haruspeak.batch.dto.request.DiaryRequest;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailySummaryClient {

    private final RestTemplate restTemplate;

    @Value("${fastapi.base-url}${fastapi.endpoint.daily-summary}")
    private String industryVectorUrl;

    /**
     * 오늘 작성한 일기 내용으로 api를 호출하여 제목과 요약을 받아오는 client
     * @param todayDiaryContent 오늘 작성한 모든 일기 내용
     * @return DailySummaryResponse 제목, 요약
     */
    public DailySummaryResponse getDailySummary(String todayDiaryContent) {
        return ApiClientHelper.post(
                restTemplate,
                industryVectorUrl,
                new DiaryRequest(todayDiaryContent),
                DailySummaryResponse.class
        );
    }
}


