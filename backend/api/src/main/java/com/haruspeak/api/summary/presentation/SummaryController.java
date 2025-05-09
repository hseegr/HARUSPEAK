package com.haruspeak.api.summary.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.summary.application.SummaryService;
import com.haruspeak.api.summary.dto.request.DailySummaryCreateRequest;
import com.haruspeak.api.summary.dto.response.DailySummaryCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
@Tag(
        name = "Summary",
        description = "하루 일기 관련 API"
)
public class SummaryController {
    private final SummaryService summaryService;

    @PostMapping("/{summaryId}/content/regenerate")
    @Operation(summary = "하루일기 요약 재생성", description = "하루일기 요약 내용을 AI 를 통해 재생성합니다.")
    public ResponseEntity<DailySummaryCreateResponse> regenerateDailySummary (
            @RequestBody(required = true) DailySummaryCreateRequest dscr
//            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/daily-summary";
        return ResponseEntity.ok(summaryService.regenerateDailySummary(uri, dscr));
    }

    @PostMapping("/{summaryId}/image/regenerate")
    @Operation(summary = "하루일기 썸네일 재생성", description = "하루일기 썸네일을 AI 를 통해 재생성합니다.")
    public ResponseEntity<Void> regenerateDailyThumbnail (
            @PathVariable Integer summaryId,
            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/daily-thumbnail";
        summaryService.queueRegenerateDailyThumbnail(summaryId, userId);
        return ResponseEntity.ok().build();
    }
}



