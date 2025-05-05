package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;

import java.util.List;
import java.util.Optional;

public interface ActiveDailyMomentJpaRepositoryCustom {
    Optional<MomentDetailRaw> findMomentDetailRaw(Integer userId, Integer momentId);
    List<MomentDetailRaw> findMomentListDetailRawList(Integer userId, MomentListRequest request);
}

