package com.haruspeak.api.summary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "하루 일기 요약 수정 요청")
public record DailySummaryUpdateRequest(
        @Schema(description = "하루 일기 제목")
        @Size(max=50, message = "제목은 50자 이하여야 합니다.")
        String title,
        @Schema(description = "하루 일기 요약 내용")
        @Size(max=200, message = "내용은 200자 이하여야 합니다.")
        String content
) {
}
