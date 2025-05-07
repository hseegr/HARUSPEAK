package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.QDailySummary;
import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.summary.dto.request.SummaryListRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActiveDailySummaryQdslRepositoryImpl implements ActiveDailySummaryQdslRepository {

    private final JPAQueryFactory queryFactory;

    private static final QDailySummary summary = QDailySummary.dailySummary;

    /**
     * 사용자의 삭제되지 않은 일기 정보
     * @param userId 사용자 ID
     * @return
     * - 신규 유저 또는 작성한 적이 없는 경우 (0, 0) 반환
     * - UserSummaryStat 총 summary 수와 총 moment 수
     */
    @Override
    public UserSummaryStat calculateUserSummaryStat(int userId) {
        log.debug("사용자 일기 스탯 계산 실행 (userId={})", userId);

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                UserSummaryStat.class,
                                summary.summaryId.count(),
                                summary.momentCount.sum().coalesce(0)
                        ))
                        .from(summary)
                        .where(
                                summary.userId.eq(userId),
                                summary.isDeleted.eq(false)
                        )
                        .groupBy(summary.userId)
                        .fetchOne()
        ).orElse(new UserSummaryStat(0, 0));
    }

    /**
     * 하루 일기 정보
     * @param summaryId 하루 일기 ID
     * @return SummaryDetailRaw
     */
    @Override
    public Optional<SummaryDetailRaw> findSummaryDetailRaw(int userId, int summaryId) {
        log.debug("하루 일기 조회 실행 (summaryId={})", summaryId);
        return Optional.ofNullable(queryFactory
                .select(selectSummaryDetailRaw())
                .from(summary)
                .where(
                        summary.summaryId.eq(summaryId),
                        summary.isDeleted.eq(false)
                )
                .fetchOne()
        );
    }

    @Override
    public List<SummaryDetailRaw> findSummaryListByCondition(int userId, SummaryListRequest request) {
        log.debug("하루 일기 목록 검색 실행 (userId={}, request={})", userId, request);

        return queryFactory
                .select(selectSummaryDetailRaw())
                .from(summary)
                .where(buildSearchConditions(userId, request))
                .orderBy(summary.writeDate.desc())
                .limit(request.getLimit()+1)
                .fetch()
                ;

    }


    /**
     * select 공통 구문
     * @return Expression<SummaryDetailRaw>
     */
    private Expression<SummaryDetailRaw> selectSummaryDetailRaw() {
        return Projections.constructor(
                SummaryDetailRaw.class,
                summary.summaryId,
                summary.writeDate,
                summary.imageUrl,
                summary.title,
                summary.content,
                summary.imageGenerateCount,
                summary.contentGenerateCount,
                summary.momentCount
        );
    }


    /**
     * 검색 조건에 맞춰 Where 구절 생성
     * @param userId 사용자ID
     * @param request 하루 일기 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildSearchConditions(Integer userId, SummaryListRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(summary.userId.eq(userId))
                .and(summary.isDeleted.eq(false));

        if (request.getBefore() != null) {
            builder.and(summary.writeDate.loe(request.getBefore().toLocalDate()));
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            builder.and(summary.writeDate.between(
                    request.getStartDate(),
                    request.getEndDate()
            ));
        }

        return builder;
    }
}
