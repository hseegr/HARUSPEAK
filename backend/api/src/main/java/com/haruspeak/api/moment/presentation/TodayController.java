package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.common.exception.ValidErrorResponse;
import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.TodayService;
import com.haruspeak.api.moment.dto.request.MomentUpdateRequest;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import com.haruspeak.api.moment.dto.response.TodayMomentListResponse;
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
@RequestMapping("/api/today")
@Tag(
        name = "Today",
        description = "오늘 순간 일기 관련 API"
)
public class TodayController {

    private final TodayService todayService;

    @PostMapping("")
    @Operation(
            summary = "오늘의 순간 일기 작성",
            description = "오늘 순간의 내용, 이미지를 기록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ValidErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> writeMoment(@Valid @RequestBody MomentWriteRequest request, @AuthenticatedUser Integer userId){
        log.info("[POST] api/today 오늘의 순간 일기 작성 요청 (userId={})", userId);
        todayService.saveMoment(request,userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/time/{time}")
    @Operation(
            summary = "오늘의 순간 일기 수정",
            description = "오늘 순간의 내용 또는 작성 시각을 수정하거나, 이미지를 삭제합니다.",
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
    public ResponseEntity<Void> modifyMoment(@PathVariable String time, @Valid @RequestBody MomentUpdateRequest request, @AuthenticatedUser Integer userId){
        log.info("[PATCH] api/today/time/{} 오늘의 순간 일기 수정 요청 (userId={}", time, userId);
        todayService.updateMoment(time, request,userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/time/{time}")
    @Operation(
            summary = "오늘의 순간 일기 삭제",
            description = "작성했던 특정 순간 일기를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(implementation = ValidErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> deleteMoment(@PathVariable String time, @AuthenticatedUser Integer userId){
        log.info("[DELETE] api/today/time/{} 오늘의 순간 일기 삭제 요청 (userId={}", time, userId);
        todayService.removeMoment(time,userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    @Operation(
            summary = "오늘의 순간 일기 조회",
            description = "오늘 작성한 모든 순간 일기를 조회합니다."
    )
    public ResponseEntity<TodayMomentListResponse> getTodayMoments(@AuthenticatedUser Integer userId){
        log.info("[GET] api/today 오늘의 순간 일기 전체 조회 (userId={}",userId);
        TodayMomentListResponse result = todayService.getTodayMoments(userId);
        return ResponseEntity.ok(result);
    }
}
