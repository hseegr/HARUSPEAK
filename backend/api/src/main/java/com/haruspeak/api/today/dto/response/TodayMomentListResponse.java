package com.haruspeak.api.today.dto.response;

import com.haruspeak.api.today.dto.TodayMoment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘의 순간 일기 목록 응답")
public record TodayMomentListResponse(
        @Schema(description = "오늘의 순간 일기 목록")
        List<TodayMoment> data,
        @Schema(description = "데이터 개수", example = "1")
        Integer dataCount
) {
}
