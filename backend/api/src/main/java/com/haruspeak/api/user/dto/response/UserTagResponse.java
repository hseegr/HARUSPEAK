package com.haruspeak.api.user.dto.response;

import com.haruspeak.api.user.dto.UserTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 태그 목록")
public record UserTagResponse(
        @Schema(description = "태그 목록")
        List<UserTag> tags,
        @Schema(description = "총 태그 수", example = "1")
        int tagCount
) { }