package com.haruspeak.api.summary.domain.repository;

import com.haruspeak.api.summary.domain.QActiveDailySummary;
import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActiveSummaryJpaRepositoryCustomImpl implements ActiveSummaryJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QActiveDailySummary summary = QActiveDailySummary.activeDailySummary;

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
                        .where(summary.userId.eq(userId))
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
                .select(Projections.constructor(
                        SummaryDetailRaw.class,
                        summary.summaryId,
                        summary.writeDate,
                        summary.imageUrl,
                        summary.title,
                        summary.content,
                        summary.imageGenerateCount,
                        summary.contentGenerateCount,
                        summary.momentCount
                ))
                .from(summary)
                .where(summary.summaryId.eq(summaryId))
                .fetchOne()
        );
    }
}
