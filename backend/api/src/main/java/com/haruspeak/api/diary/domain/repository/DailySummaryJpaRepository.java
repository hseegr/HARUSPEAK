package com.haruspeak.api.diary.domain.repository;

import com.haruspeak.api.diary.domain.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySummaryJpaRepository extends JpaRepository<DailySummary,Integer> {
}
