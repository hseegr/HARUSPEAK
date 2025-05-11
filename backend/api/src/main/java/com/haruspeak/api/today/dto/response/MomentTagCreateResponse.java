package com.haruspeak.api.today.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "작성한 순간 일기에 대한 태그 추천 응답")
public record MomentTagCreateResponse(
        @Schema(description = "추천 태그 목록", example = "[\"태그1\", \"태그2\"]")
        List<String> recommendTags
) {
}
