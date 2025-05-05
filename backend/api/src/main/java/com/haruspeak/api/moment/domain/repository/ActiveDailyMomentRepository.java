package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.dto.MomentDetailRaw;
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

    public Optional<MomentDetailRaw> findMomentDetail(int userId, int momentId){
        return activeDailyMomentJpaRepositoryCustomImpl.findMomentDetail(userId, momentId);
    }

    public List<MomentDetailRaw> findMomentListByCondition(int userId, MomentListRequest request){
        return activeDailyMomentJpaRepositoryCustomImpl.findMomentListByCondition(userId, request);
    }

    public List<MomentDetailRaw> findMomentListBySummaryId(int userId, int summaryId){
        return activeDailyMomentJpaRepositoryCustomImpl.findMomentListBySummaryId(userId, summaryId);
    }
}
