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
      log.debug("🐛 오늘의 제목 및 요약 내용 생성 요청");
      try {
          return dailySummaryClient.getDailySummary(totalTodayContent);
      }catch (Exception e) {
          log.error("💥 오늘의 요약 생성 중 에러 발생: {}", e.getMessage());
          throw e;
      }
    }

    public String generateThumbnailUrl(String todaySummaryContent){
        log.debug("🐛 오늘의 썸네일 생성 및 S3 저장 후 S3 URL 요청");
        try {
            return uploadThumbnailAndGetS3Url(generateThumbnailBase64(todaySummaryContent));    
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 생성 및 S3 저장 중 오류 발생: {}", e.getMessage());
            throw e;
        }
        
    }

    private String generateThumbnailBase64(String totalTodayContent) {
        try {
            return dailyThumbnailClient.getDailyThumbnail(totalTodayContent).base64();
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 생성 요청 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    private String uploadThumbnailAndGetS3Url(String base64){
        try {
            return s3Service.uploadImagesAndGetUrls(base64);
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 S3 저장 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }
}
