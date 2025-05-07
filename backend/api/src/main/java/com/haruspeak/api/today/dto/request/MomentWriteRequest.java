package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "오늘 순간 일기 작성 요청 정보")
public record MomentWriteRequest(
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        @NotNull @Size(max = 500)
        String content,
        @Schema(description = "이미지 목록", example = "[\"base64 주소\"]")
        @NotNull @Size(max = 10)
        List<String> images
) {
}
