package com.haruspeak.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 태그 정보")
public record UserTag(
        @Schema(description = "사용자 태그 ID", example = "1")
        int userTagId,
        @Schema(description = "태그 이름", example = "태그 이름")
        String name,
        @Schema(description = "태그 사용 횟수", example = "3")
        int count
) { }