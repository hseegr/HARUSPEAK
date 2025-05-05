package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.common.exception.ValidErrorResponse;
import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.MomentService;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
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

/**
 * moment 조회
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moment")
@Tag(
        name = "Moment",
        description = "순간 일기 조회 API"
)
public class MomentController {
    private final MomentService activeDailyMomentService;

    /**
     * 순간 일기 상세 조회
     * @param momentId 순간일기ID
     * @param userId 사용자ID
     * @return MomentDetailResponse
     */
    @GetMapping("/{momentId}")
    @Operation(
            summary = "순간 일기 상세 조회",
            description = "순간 일기 상세 정보(ID, 작성시각, 이미지주소목록, 일기내용, 태그목록)를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<MomentDetailResponse> getMomentDetail(@PathVariable Integer momentId, @AuthenticatedUser Integer userId) {
        log.info("[GET] api/moment/{} 순간 일기 상세 조회 요청 (userId={})", momentId, userId);
        return ResponseEntity.ok(activeDailyMomentService.getMomentDetail(userId, momentId));
    }

    /**
     * 순간 일기 목록 조회
     * @param request 요청 parameter
     * @param userId 사용자ID
     * @return MomentListResponse
     */
    @GetMapping("")
    @Operation(
            summary = "순간 일기 목록 조회",
            description = "순간 일기 목록(ID, 작성시각, 이미지주소목록, 일기내용, 태그목록)을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ValidErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<MomentListResponse> getMomentList(
            @Valid @ModelAttribute MomentListRequest request,
            @AuthenticatedUser Integer userId
    ){
        log.info("[GET] api/moment/ 순간 일기 목록 조회 요청 (request={}, userId={})", request.toString(), userId);
        return ResponseEntity.ok(activeDailyMomentService.getMomentList(request,userId));
    }
}
