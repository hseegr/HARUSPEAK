package com.haruspeak.api.summary.dto.response;

import com.haruspeak.api.common.dto.ResInfo;
import com.haruspeak.api.summary.dto.SummaryDetail;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "하루 일기 목록 조회 응답")
public record SummaryListResponse(
        @Schema(description = "하루 일기 목록")
        List<SummaryDetail> data,
        @Schema(description = "응답 정보")
        ResInfo resInfo
) {
}
