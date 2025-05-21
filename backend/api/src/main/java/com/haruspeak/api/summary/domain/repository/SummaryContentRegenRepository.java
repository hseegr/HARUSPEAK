package com.haruspeak.api.summary.domain.repository;

public interface SummaryContentRegenRepository {

    void saveSummaryRegenState(int userId, int summaryId);

    boolean isSummaryGenerating(int userId, int summaryId);

    void deleteSummaryRegenState(int userId, int summaryId);

}
