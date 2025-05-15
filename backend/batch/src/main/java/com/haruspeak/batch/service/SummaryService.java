package com.haruspeak.batch.service;

import com.haruspeak.batch.common.client.fastapi.DailySummaryClient;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final DailySummaryClient dailySummaryClient;

    public DailySummaryResponse generateDailySummary(String totalTodayContent) {
      log.debug("🐛 오늘의 제목 및 요약 내용 생성 요청");
      try {
          return dailySummaryClient.getDailySummary(totalTodayContent);
      }catch (Exception e) {
          log.error("💥 오늘의 요약 생성 중 에러 발생: {}", e.getMessage());
          throw e;
      }
    }

}
