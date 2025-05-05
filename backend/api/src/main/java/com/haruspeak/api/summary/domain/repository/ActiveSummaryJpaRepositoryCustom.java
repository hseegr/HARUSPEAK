package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.dto.UserSummaryStat;

public interface ActiveSummaryJpaRepositoryCustom {

    UserSummaryStat calculateUserSummaryStat(int userId);

}