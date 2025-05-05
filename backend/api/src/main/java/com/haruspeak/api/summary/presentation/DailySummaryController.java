package com.haruspeak.api.summary.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.common.exception.ValidErrorResponse;
import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.summary.application.DailySummaryService;
import com.haruspeak.api.summary.dto.request.DailySummaryUpdateRequest;
import com.haruspeak.api.summary.dto.response.DiaryDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;

    @PatchMapping("/{summaryId}")
    @Operation(
            summary = "하루 일기 수정 조회",
            description = "하루 일기 제목, 요약 내용을 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ValidErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> modifyDailySummary(@PathVariable Integer summaryId, @RequestBody @Valid DailySummaryUpdateRequest request, @AuthenticatedUser Integer userId){
        log.info("[PATCH] api/summary/{} 하루 일기 요약 수정 요청 (userId={})", summaryId, userId);
        dailySummaryService.updateDailySummary(summaryId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{summaryId}")
    @Operation(
            summary = "하루 일기 상세 조회",
            description = "하루 일기 요약, 순간 일기 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = DiaryDetailResponse.class))
                    )
            }
    )
    public ResponseEntity<DiaryDetailResponse> getDiaryDetailBySummaryId(@PathVariable Integer summaryId, @AuthenticatedUser Integer userId){
        log.info("[GET] api/summary/{} 하루 일기 상세 정보 요청 (userId={})", summaryId, userId);
        return ResponseEntity.ok(dailySummaryService.getDiaryDetail(userId, summaryId));
    }



}
