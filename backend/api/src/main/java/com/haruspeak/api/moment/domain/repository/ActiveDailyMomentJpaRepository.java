package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveDailyMomentJpaRepository extends JpaRepository<ActiveDailyMoment,Integer> {
}
