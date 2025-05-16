package com.haruspeak.api.summary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "하루 일기 요약 수정 요청")
public record DailySummaryUpdateRequest(
        @Schema(description = "하루 일기 제목")
        String title,
        @Schema(description = "하루 일기 요약 내용")
        String content
) {
}
