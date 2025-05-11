package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.SummaryThumnailRegenState;

public interface SummaryThumnailRegenStateRedisRepository {
    SummaryThumnailRegenState findBySummaryId(int userId, int summaryId);
    boolean isGenereatingOfSummary(int userId, int summaryId);
}
