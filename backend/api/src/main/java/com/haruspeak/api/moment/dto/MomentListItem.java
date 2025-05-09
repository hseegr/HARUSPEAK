package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "순간 일기 상세 정보 - 순번 포함")
public record MomentListItem(
        @Schema(description = "하루일기ID", example = "1")
        int summaryId,
        @Schema(description = "순간일기ID", example = "1")
        int momentId,
        @Schema(description = "순간일기의 하루내 순번", example = "1")
        int orderInDay,
        @Schema(description = "작성시각", example = "2025-05-01T11:22:33")
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        @Schema(description = "이미지 개수", example = "1")
        int imageCount,
        @Schema(description = "이미지 주소 목록", example = "[\"https://s3.example.com/image.png\"]")
        List<String> images,
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        String content,
        @Schema(description = "태그 개수", example = "1")
        Integer tagCount,
        @Schema(description = "태그 목록", example = "[\"태그1\"]")
        List<String> tags
) {
}
