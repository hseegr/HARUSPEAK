package com.haruspeak.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 일기 스탯")
public record UserStatResponse (
        @Schema(description = "오늘 작성한 일기 개수")
        int todayCount,
        @Schema(description = "총 하루 일기 수")
        int totalMomentCount,
        @Schema(description = "총 순간 일기 수")
        int totalDiaryCount
) {
}
