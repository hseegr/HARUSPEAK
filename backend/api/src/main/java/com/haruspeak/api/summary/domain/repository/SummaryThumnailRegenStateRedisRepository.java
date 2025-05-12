package com.haruspeak.api.summary.domain.repository;

import java.util.Map;

public interface SummaryThumnailRegenStateRedisRepository {
    Object findBySummaryId(int userId, int summaryId);
    boolean isGenereatingOfSummary(int userId, int summaryId);
}
