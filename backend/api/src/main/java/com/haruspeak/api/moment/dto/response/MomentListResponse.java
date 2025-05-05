package com.haruspeak.api.moment.dto.response;

import com.haruspeak.api.moment.dto.ResInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "순간 일기 목록 조회 응답")
public record MomentListResponse(
        @Schema(description = "순간 일기 상세 정보 목록")
        List<MomentDetailResponse> data,
        @Schema(description = "응답 정보")
        ResInfo resInfo
) {}

