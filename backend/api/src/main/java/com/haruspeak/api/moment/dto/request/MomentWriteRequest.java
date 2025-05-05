package com.haruspeak.api.moment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "오늘 순간 일기 작성 요청 정보")
public record MomentWriteRequest(
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        @NotNull
        String content,
        @Schema(description = "이미지 목록", example = "[\"이미지주소1\"]")
        @NotNull
        List<String> images
) {
}
