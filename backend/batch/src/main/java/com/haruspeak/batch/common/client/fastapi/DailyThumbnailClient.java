package com.haruspeak.batch.common.client.fastapi;

import com.haruspeak.batch.common.client.ApiClientHelper;
import com.haruspeak.batch.diary.dto.request.DiaryRequest;
import com.haruspeak.batch.diary.dto.response.DailyThumbnailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyThumbnailClient {

    private final RestTemplate restTemplate;

    @Value("${fastapi.base-url}${fastapi.endpoint.generate-thumbnail}")
    private String industryVectorUrl;

    /**
     * 오늘의 모든 일기 요약 내용으로 api를 호출하여 오늘 하루의 썸네일을 생성하여 받아오는 client
     * @param todaySummary 오늘 작성한 일기의 요약 내용
     * @return DailyThumbnailResponse Base64로 변환된 오늘의 썸네일
     */
    public DailyThumbnailResponse getDailyThumbnail(String todaySummary) {
        return ApiClientHelper.post(
                restTemplate,
                industryVectorUrl,
                new DiaryRequest(todaySummary),
                DailyThumbnailResponse.class
        );
    }
}


