package com.haruspeak.api.moment.dto.response;

import com.haruspeak.api.common.dto.ResInfo;
import com.haruspeak.api.moment.dto.MomentListItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "순간 일기 목록 조회 응답")
public record MomentListResponse(
        @Schema(description = "순간 일기 검색 결과 목록")
        List<MomentListItem> data,
        @Schema(description = "응답 정보")
        ResInfo resInfo
) {}

