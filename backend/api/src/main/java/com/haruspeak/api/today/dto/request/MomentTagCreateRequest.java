package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "태그 추천 요청")
public record MomentTagCreateRequest(
        @Schema(description = "현재 작성된 태그 목록", example = "[\"태그1\", \"태그2\"]")
        List<String> tags,
        @Schema(description = "순간 일기 생성시각", example = "2025-05-01T01:02:03")
        String createdAt,
        @Schema(description = "순간 일기 내용", example = "일기 내용 ㅡㅡ")
        String content
) {
}
