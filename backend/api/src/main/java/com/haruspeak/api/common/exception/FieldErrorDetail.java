package com.haruspeak.api.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "요청 필드값 에러 응답")
public record FieldErrorDetail(
        @Schema(description = "필드 이름", example = "요청값 에러 발생 필드")
        String field,
        @Schema(description = "에러 메시지", example = "요청값 에러 발생 필드값")
        String message
) {}