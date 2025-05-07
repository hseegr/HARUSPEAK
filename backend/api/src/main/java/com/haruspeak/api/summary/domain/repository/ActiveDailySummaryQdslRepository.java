package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.summary.dto.request.SummaryListRequest;

import java.util.List;
import java.util.Optional;

public interface ActiveDailySummaryQdslRepository {

    UserSummaryStat calculateUserSummaryStat(int userId);

    Optional<SummaryDetailRaw> findSummaryDetailRaw(int userId, int momentId);

    List<SummaryDetailRaw> findSummaryListByCondition(int userId, SummaryListRequest request);

}