package com.haruspeak.api.today.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "태그 업데이트 요청 정보")
public record TagUpdateRequest(
        @Schema(description = "생성 시각", example = "2025-05-01T01:01:01")
        String createdAt,
        @Schema(description = "태그 목록", example = "[\"태그\"]")
        List<String> tags
) {
}
