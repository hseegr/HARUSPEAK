package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentJpaRepositoryCustomImpl implements ActiveDailyMomentJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    /**
     * 순간 일기 상세보기
     * @param momentId
     * @return
     */
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

    /**
     * 순간 일기 리스트 불러오기
     * @param userId
     * @param request
     * @return
     */
    @Override
    public List<ActiveDailyMoment> getMomentList(Integer userId, MomentListRequest request) {
        QActiveDailyMoment moment = QActiveDailyMoment.activeDailyMoment;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(moment.userId.eq(userId));

        if (request.before() != null) {
            LocalDateTime beforeDateTime = request.before();
            builder.and(moment.momentTime.lt(beforeDateTime));
        }

        if (request.startDate() != null && request.endDate() != null) {
            builder.and(moment.momentTime.between(
                    request.startDate().atStartOfDay(),
                    request.endDate().atTime(23, 59, 59)
            ));
        }

        if (request.userTags() != null && !request.userTags().isEmpty()) {
            for (String userTag : request.userTags()) {
                builder.and(moment.tags.like("%" + userTag + "%"));
            }
        }

        return queryFactory
                .selectFrom(moment)
                .where(builder)
                .orderBy(moment.momentTime.desc())
                .limit(request.limit() != null ? request.limit() : 30)
                .fetch();
    }
}
