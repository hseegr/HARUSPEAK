package com.haruspeak.api.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "공통 에러 응답")
public record ErrorResponse(
        @Schema(description = "커스텀 에러 코드")
        int code,
        @Schema(description = "커스텀 에러 메시지")
        String message,
        @Schema(description = "응답 시각", example = "2025-05-01T22:33:11")
        String timestamp,
        @Schema(description = "상세 메세지", nullable = true)
        String details
) {}
