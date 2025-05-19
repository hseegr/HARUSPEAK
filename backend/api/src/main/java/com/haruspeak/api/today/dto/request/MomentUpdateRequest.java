package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "순간 일기 수정 요청 정보")
public record MomentUpdateRequest(
        @Schema(description = "생성 시각", example = "2025-05-01T01:01:01")
        @NotBlank(message = "생성 시각은 필수 입니다.")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?$",
                message = "생성 시각 형식이 올바르지 않습니다. (예: 2025-05-19T07:45:16 또는 2025-05-19T07:45:16.123456)"
        )
        String createdAt,

        @Schema(description = "작성 시각(사용자 지정)", example = "2025-05-01T01:01:01")
        @NotBlank(message = "작성 시각은 필수 입니다.")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?$",
                message = "작성 시각 형식이 올바르지 않습니다. (예: 2025-05-19T07:45:16 또는 2025-05-19T07:45:16.123456)"
        )
        String momentTime,

        @Schema(description = "삭제 후 남길 이미지 목록", example = "[\"삭제하지 않을 이미지\"]")
        @NotNull(message = "이미지 목록이 NULL이어서는 안 됩니다.")
        @Size(max = 10, message = "이미지는 10개를 초과할 수 없습니다.")
        List<String> images,

        @Schema(description = "삭제할 이미지 목록", example = "[\"삭제할 이미지\"]")
        @NotNull(message = "삭제할 이미지 목록이 NULL이어서는 안 됩니다. 삭제할 이미지가 없는 경우 빈 목록으로 요청해 주세요.")
        @Size(max = 10, message = "삭제할 이미지가 10개를 초과할 수 없습니다.")
        List<String> deletedImages,

        @Schema(description = "수정할 일기 내용", example = "일기 내용입니다.")
        @NotNull(message = "일기 내용이 NULL이어서는 안 됩니다.")
        @Size(max = 500, message = "일기 내용은 500글자를 초과할 수 없습니다.")
        String content,

        @Schema(description = "태그 목록", example = "[\"태그\"]")
        @NotNull(message = "태그 목록이 NULL이어서는 안 됩니다.")
        @Size(max = 10, message = "태그는 10개를 초과할 수 없습니다.")
        List<String> tags
) {
}
