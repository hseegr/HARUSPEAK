package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentRepository {

    private final ActiveDailyMomentJpaRepository activeDailyMomentJpaRepository;
    private final ActiveDailyMomentJpaRepositoryCustomImpl activeDailyMomentJpaRepositoryCustomImpl;

    public Optional<MomentDetailRaw> getMomentDetailRaw(Integer momentId){
        return activeDailyMomentJpaRepositoryCustomImpl.getMomentDetailRaw(momentId);
    }
}
