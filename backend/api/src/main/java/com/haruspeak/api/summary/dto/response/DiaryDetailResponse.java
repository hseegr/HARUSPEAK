package com.haruspeak.api.summary.dto.response;

import com.haruspeak.api.moment.dto.MomentListItem;
import com.haruspeak.api.summary.dto.SummaryDetail;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "하루 일기 상세 정보")
public record DiaryDetailResponse(
        @Schema(description = "하루 일기 정보")
        SummaryDetail summary,
        @Schema(description = "순간 일기 목록")
        List<MomentListItem> moments
) {
}
