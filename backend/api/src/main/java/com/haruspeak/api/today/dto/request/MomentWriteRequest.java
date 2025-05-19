package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "오늘 순간 일기 작성 요청 정보")
public record MomentWriteRequest(
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        @NotNull(message = "내용이 NULL이어서는 안 됩니다.")
        @Size(max = 500, message = "내용은 500글자를 초과할 수 없습니다.")
        String content,

        @Schema(description = "이미지 목록", example = "[\"base64 주소\"]")
        @NotNull(message = "이미지 목록이 NULL이어서는 안 됩니다.")
        @Size(max = 10, message = "이미지는 10개를 초과할 수 없습니다.")
        List<String> images
) {
}
