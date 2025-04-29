package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListDetail;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;

import java.util.List;
import java.util.Optional;

public interface ActiveDailyMomentJpaRepositoryCustom {

    Optional<MomentDetailRaw> getMomentDetailRaw(
        Integer momentId
    );

    List<ActiveDailyMoment> getMomentList(Integer userId, MomentListRequest request);
}
