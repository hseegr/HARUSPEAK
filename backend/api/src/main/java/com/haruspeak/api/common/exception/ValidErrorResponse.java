package com.haruspeak.api.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "요청 필드값 에러 응답")
public record ValidErrorResponse(
        @Schema(description = "에러 코드", example = "40001")
        int errorCode,
        @Schema(description = "에러 메시지", example = "필수 필드 누락")
        String message,
        @Schema(description = "요청 시각")
        LocalDateTime timestamp,
        @Schema(description = "필드별 오류 정보")
        List<FieldErrorDetail> fieldErrors
) {}
