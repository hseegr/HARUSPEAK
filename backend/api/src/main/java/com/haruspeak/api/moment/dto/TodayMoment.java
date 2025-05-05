package com.haruspeak.api.moment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘의 순간 일기")
public record TodayMoment(
        @Schema(description = "생성 시각", example = "2025-05-01T01:00:00")
        String createdAt,
        @Schema(description = "작성 시각", example = "2025-05-01T00:00:00")
        String momentTime,
        @Schema(description = "이미지 목록", example = "[\"이미지 주소\"]")
        List<String> images,
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        String content,
        @Schema(description = "태그 목록", example = "[\"태그\"]")
        List<String> tags
) {
}
