package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.SummaryThumbnailRegenState;

public interface SummaryThumnailRegenStateRedisRepository {
    SummaryThumbnailRegenState findBySummaryId(int userId, int summaryId);
    boolean isGenereatingOfSummary(int userId, int summaryId);
}
