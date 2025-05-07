package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.summary.dto.request.DailySummaryCreateRequest;
import com.haruspeak.api.summary.dto.request.DailyThumbnailCreateRequest;
import com.haruspeak.api.summary.dto.response.DailySummaryCreateResponse;
import com.haruspeak.api.summary.dto.response.DailyThumbnailCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final FastApiClient fastApiClient;

    // [API] AI 하루일기 요약 재생성
    @Transactional
    public DailySummaryCreateResponse regenerateDailySummary (String uri, DailySummaryCreateRequest dscr) {
        // ai 서버에 프론트 요청값 전달 후 반환 받기
        String summary = fastApiClient.getPrediction(uri, dscr);
        return new DailySummaryCreateResponse(summary);
    }

    // [API] AI 하루일기 썸네일 재생성
    @Transactional
    public DailyThumbnailCreateResponse regenerateDailyThumbnail (String uri, DailyThumbnailCreateRequest dtcr) {
        // ai 서버에 프론트 요청값 전달 후 반환 받기
        String base64 = fastApiClient.getPrediction(uri, dtcr);
        return new DailyThumbnailCreateResponse(base64);
    }
}


