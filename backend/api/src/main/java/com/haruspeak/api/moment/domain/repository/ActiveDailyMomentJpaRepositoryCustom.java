package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;

import java.util.List;
import java.util.Optional;

public interface ActiveDailyMomentJpaRepositoryCustom {
    Optional<MomentDetailRaw> findMomentDetail(int userId, int momentId);
    List<MomentDetailRaw> findMomentListByCondition(int userId, MomentListRequest request);
    List<MomentDetailRaw> findMomentListBySummaryId(int userId, int summaryId);
}

