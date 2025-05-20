package com.haruspeak.api.summary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "하루 일기 요약 수정 요청")
public record DailySummaryUpdateRequest(
        @Schema(description = "하루 일기 제목")
        @NotBlank(message = "제목을 입력해 주세요.")
        @Size(max = 30, message = "제목은 30글자 이내로 작성해 주세요.")
        String title,
        @NotNull(message = "요약 내용이 NULL이면 안됩니다.")
        @Size(max = 200, message = "요약 내용은 200글자 이내로 작성해 주세요.")
        @Schema(description = "하루 일기 요약 내용")
        String content
) {
}
