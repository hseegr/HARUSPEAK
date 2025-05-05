package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.DailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailySummaryRepository {
    private final DailySummaryJpaRepository dailySummaryJpaRepository;

    public Optional<DailySummary> findById(Integer summaryId){
        return dailySummaryJpaRepository.findById(summaryId);
    }
}
