package com.haruspeak.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인된 사용자 정보")
public record LoginUserResponse(
        @Schema(description = "사용자 ID", example = "1")
        int userId,
        @Schema(description = "사용자 이름", example = "이름")
        String name
) { }
