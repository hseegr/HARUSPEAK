package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.SummaryThumnailRegenState;

public interface SummaryThumnailRegenStateRedisRepository {
    void save(String userId, String summaryId, SummaryThumnailRegenState state);
    void delete(String userId, String summaryId);
    SummaryThumnailRegenState findBySummaryId(int userId, int summaryId);
    boolean isGenereatingOfSummary(int userId, int summaryId);
}
