package com.haruspeak.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "목록 조회 응답 정보")
public record ResInfo(
        @Schema(description = "데이터 개수", example = "30")
        Integer dataCount,
        @Schema(description = "다음 커서", example = "2025-05-01T11:22:33")
        LocalDateTime nextCursor,
        @Schema(description = "다음 데이터 존재 여부", example = "true")
        Boolean hasMore
) {}
