package com.haruspeak.api.summary.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.summary.application.DailySummaryService;
import com.haruspeak.api.summary.dto.request.DailySummaryCreateRequest;
import com.haruspeak.api.summary.dto.request.DailyThumbnailCreateRequest;
import com.haruspeak.api.summary.dto.response.DailySummaryCreateResponse;
import com.haruspeak.api.summary.dto.response.DailyThumbnailCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
@Tag(
        name = "Summary",
        description = "하루 일기 관련 API"
)
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;

    @PostMapping("/{summaryId}/content/regenerate")
    @Operation(summary = "하루일기 요약 재생성", description = "하루일기 요약 내용을 AI 를 통해 재생성합니다.")
    public ResponseEntity<DailySummaryCreateResponse> regenerateDailySummary (
            @RequestBody(required = true) DailySummaryCreateRequest dscr
//            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/daily-summary";
        return ResponseEntity.ok(dailySummaryService.regenerateDailySummary(uri, dscr));
    }

    @PostMapping("/{summaryId}/image/regenerate")
    @Operation(summary = "하루일기 썸네일 재생성", description = "하루일기 썸네일을 AI 를 통해 재생성합니다.")
    public ResponseEntity<DailyThumbnailCreateResponse> regenerateDailyThumbnail (
            @RequestBody(required = true) DailyThumbnailCreateRequest dtcr
//            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/daily-thumbnail";
        return ResponseEntity.ok(dailySummaryService.regenerateDailyThumbnail(uri, dtcr));
    }
}



