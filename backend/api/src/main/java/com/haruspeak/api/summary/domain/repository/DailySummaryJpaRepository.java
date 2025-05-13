package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySummaryJpaRepository extends JpaRepository<DailySummary,Integer> {
}
