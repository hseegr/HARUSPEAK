package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "순간 일기 수정 요청 정보")
public record MomentUpdateRequest(
        @Schema(description = "작성 시각(사용자 지정)", example = "2025-05-01T01:01:01")
        String momentTime,
        @Schema(description = "삭제 후 남길 이미지 목록", example = "[\"삭제하지 않을 이미지\"]")
        List<String> images,
        @Schema(description = "삭제할 이미지 목록", example = "[\"삭제할 이미지\"]")
        List<String> deletedImages,
        @Schema(description = "수정할 일기 내용", example = "일기 내용입니다.")
        String content,
        @Schema(description = "태그 목록", example = "[\"태그\"]")
        List<String> tags
) {
}
