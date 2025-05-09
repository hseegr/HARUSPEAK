package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListItemRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;

import java.util.List;
import java.util.Optional;

public interface ActiveDailyMomentQdslRepository {
    Optional<MomentDetailRaw> findMomentDetail(int userId, int momentId);
    List<MomentListItemRaw> findMomentListByCondition(int userId, MomentListRequest request);
    List<MomentListItemRaw> findMomentListBySummaryId(int userId, int summaryId);
}

