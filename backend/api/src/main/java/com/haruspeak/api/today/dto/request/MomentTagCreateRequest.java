package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "태그 추천 요청")
public record MomentTagCreateRequest(
        @Schema(description = "현재 작성된 태그 목록", example = "[\"태그1\", \"태그2\"]")
        @NotNull(message = "리스트 형식을 맞춰 주세요. 작성된 태그 목록이 NULL이어서는 안 됩니다.")
        @Size(max = 2, message = "태그 생성 요청은 작성된 태그가 3개 미만일 경우 가능 합니다.")
        List<String> tags,

        @Schema(description = "순간 일기 생성 시각", example = "2025-05-01T01:02:03")
        @NotBlank(message = "생성 시각은 필수 입니다.")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?$",
                message = "생성 시각 형식이 올바르지 않습니다. (예: 2025-05-19T07:45:16 또는 2025-05-19T07:45:16.123456)"
        )
        String createdAt,

        @Schema(description = "순간 일기 내용", example = "일기 내용 ㅡㅡ")
        @NotBlank(message = "태그를 생성하려면 내용을 먼저 작성해 주세요.")
        String content,

        @Schema(description = "수정 페이지인지 여부", example = "false")
        @NotNull(message = "isEditPage가 NULL이어서는 안됩니다.")
        Boolean isEditPage
) {
}
