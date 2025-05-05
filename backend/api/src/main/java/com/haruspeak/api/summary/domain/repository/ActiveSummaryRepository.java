package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveSummaryRepository {

    private final ActiveSummaryJpaRepositoryCustomImpl summaryRepositoryCustom;

    public UserSummaryStat calculateUserSummaryStat(int userId) {
        return summaryRepositoryCustom.calculateUserSummaryStat(userId);
    }

    public Optional<SummaryDetailRaw> findSummaryDetailRaw(int userId, int summaryId) {
        return summaryRepositoryCustom.findSummaryDetailRaw(userId, summaryId);
    }
}
