package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentRepository {

    private final ActiveDailyMomentJpaRepository activeDailyMomentJpaRepository;
    private final ActiveDailyMomentJpaRepositoryCustomImpl activeDailyMomentJpaRepositoryCustomImpl;

    public Optional<MomentDetailRaw> findMomentDetailRaw(Integer userId, Integer momentId){
        return activeDailyMomentJpaRepositoryCustomImpl.findMomentDetailRaw(userId, momentId);
    }

    public List<MomentListDetailRaw> findMomentList(Integer userId, MomentListRequest request){
        return activeDailyMomentJpaRepositoryCustomImpl.findMomentList(userId, request);
    }
}
