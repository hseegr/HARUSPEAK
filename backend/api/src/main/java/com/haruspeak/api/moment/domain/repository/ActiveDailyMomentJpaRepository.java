package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActiveDailyMomentJpaRepository extends JpaRepository<ActiveDailyMoment, Integer> {

    @Query(value = """
        SELECT 
            m.moment_id AS momentId, 
            ROW_NUMBER() OVER (
                PARTITION BY m.summary_id
                ORDER BY m.moment_time
            ) AS orderInDay
        FROM 
            active_daily_moments m
        WHERE 
            m.moment_id IN (:momentIds)
        """, nativeQuery = true)
    List<MomentOrder> findRanksByMomentIds(@Param("momentIds") List<Integer> momentIds);

    List<ActiveDailyMoment> findBySummaryId(int summaryId);
}
