package com.haruspeak.api.moment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "순간 일기 상세 정보")
public record MomentDetailResponse(
        @Schema(description = "순간일기ID", example = "1")
        Integer momentId,
        @Schema(description = "작성시각", example = "2025-05-01:T11:22:33")
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        @Schema(description = "이미지 주소 목록", example = "[\"이미지URL\"]")
        List<String> images,
        @Schema(description = "일기 내용", example = "일기 내용입니다.")
        String content,
        @Schema(description = "태그 목록", example = "[\"태그1\"]")
        List<String> tags
) {
}
