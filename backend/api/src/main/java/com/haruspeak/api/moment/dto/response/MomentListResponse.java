package com.haruspeak.api.moment.dto.response;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentListDetail;
import com.haruspeak.api.moment.dto.ResInfo;

import java.util.List;

public record MomentListResponse(
        List<MomentListDetail> data,
        ResInfo resInfo
) {}

