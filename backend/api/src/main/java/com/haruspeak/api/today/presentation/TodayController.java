package com.haruspeak.api.today.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.common.exception.ValidErrorResponse;
import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.today.application.TodayService;
import com.haruspeak.api.today.dto.request.MomentUpdateRequest;
import com.haruspeak.api.today.dto.request.MomentWriteRequest;
import com.haruspeak.api.today.dto.response.TodayMomentListResponse;
import com.haruspeak.api.today.dto.response.TodaySttResponse;
import com.haruspeak.api.user.dto.response.UserStatResponse;
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
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value="/voice-to-text", consumes = "multipart/form-data")
    @Operation(
            summary = "음성파일 텍스트변환",
            description = "요청된 음성파일을 텍스트로 변환하여 응답합니다."
    )
    public ResponseEntity<TodaySttResponse> transferStt(
            @RequestParam("file") MultipartFile file,
            @AuthenticatedUser Integer userId
    ){
        log.info("[POST] api/today/voice-to-text 음성파일 텍스트 변환 (userId={})", userId);
        String uri = "/ai/stt-faster-whisper";
        return ResponseEntity.ok(todayService.transferStt(uri, file, userId));
    }

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
            description = "오늘 작성한 모든 순간 일기를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = TodayMomentListResponse.class))
                    )
            }
    )
    public ResponseEntity<TodayMomentListResponse> getTodayMoments(@AuthenticatedUser Integer userId){
        log.info("[GET] api/today 오늘의 순간 일기 전체 조회 (userId={})",userId);
        TodayMomentListResponse result = todayService.getTodayMoments(userId);
        return ResponseEntity.ok(result);
    }
}
