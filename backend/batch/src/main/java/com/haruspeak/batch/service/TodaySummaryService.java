package com.haruspeak.batch.service;

import com.haruspeak.batch.common.client.fastapi.DailySummaryClient;
import com.haruspeak.batch.common.client.fastapi.DailyThumbnailClient;
import com.haruspeak.batch.common.s3.S3Service;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodaySummaryService {

    private final DailySummaryClient dailySummaryClient;
    private final DailyThumbnailClient dailyThumbnailClient;

    private final S3Service s3Service;

    public DailySummaryResponse generateDailySummary(String totalTodayContent) {
      log.debug("🐛 STEP1.PROCESS - 오늘의 제목 및 요약 내용 생성 요청");
      return dailySummaryClient.getDailySummary(totalTodayContent);
    }

    public String generateThumbnailUrl(String todaySummaryContent){
        log.debug("🐛 STEP1.PROCESS - 오늘의 썸네일 생성 및 S3 저장 후 S3 URL 요청");
        return uploadThumbnailAndGetS3Url(generateThumbnailBase64(todaySummaryContent));
    }

    private String generateThumbnailBase64(String totalTodayContent) {
        return dailyThumbnailClient.getDailyThumbnail(totalTodayContent).base64();
    }

    private String uploadThumbnailAndGetS3Url(String base64){
        return s3Service.uploadImagesAndGetUrls(base64);
    }
}
