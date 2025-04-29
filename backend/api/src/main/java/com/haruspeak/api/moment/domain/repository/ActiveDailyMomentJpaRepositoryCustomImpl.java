package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentJpaRepositoryCustomImpl implements ActiveDailyMomentJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MomentDetailRaw> getMomentDetailRaw(Integer momentId) {
        QActiveDailyMoment moment = QActiveDailyMoment.activeDailyMoment;

        return Optional.ofNullable(queryFactory.select(
                        Projections.constructor(
                                MomentDetailRaw.class,
                                moment.momentId,
                                moment.momentTime,
                                moment.imageUrls,
                                moment.content,
                                moment.tags
                        )
                )
                .from(moment)
                .where(moment.momentId.eq(momentId))
                .fetchOne());
    }
}
