package com.haruspeak.api.moment.dto.response;

import com.haruspeak.api.moment.dto.TodayMoment;

import java.util.List;

public record TodayMomentListResponse(
        List<TodayMoment> data,
        Integer dataCount
) {
}
