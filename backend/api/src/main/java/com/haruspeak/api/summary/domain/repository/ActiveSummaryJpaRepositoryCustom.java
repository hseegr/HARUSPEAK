package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;

import java.util.Optional;

public interface ActiveSummaryJpaRepositoryCustom {

    UserSummaryStat calculateUserSummaryStat(int userId);

    Optional<SummaryDetailRaw> findSummaryDetailRaw(int userId, int momentId);

}