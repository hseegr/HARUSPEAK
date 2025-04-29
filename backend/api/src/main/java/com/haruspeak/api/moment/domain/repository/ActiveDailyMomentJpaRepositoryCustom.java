package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;

import java.util.Optional;

public interface ActiveDailyMomentJpaRepositoryCustom {

    Optional<MomentDetailRaw> getMomentDetailRaw(
        Integer momentId
    );
}
