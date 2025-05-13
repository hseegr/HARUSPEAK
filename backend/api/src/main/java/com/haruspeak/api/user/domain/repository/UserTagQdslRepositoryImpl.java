package com.haruspeak.api.user.domain.repository;

import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.domain.QMomentTag;
import com.haruspeak.api.user.domain.QUserTagDetail;
import com.haruspeak.api.user.dto.UserTagRaw;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserTagQdslRepositoryImpl implements UserTagQdslRepository {

    private final JPAQueryFactory queryFactory;

    private static final QUserTagDetail userTag = QUserTagDetail.userTagDetail;
    private static final QMomentTag momentTag = QMomentTag.momentTag;
    private static final QActiveDailyMoment moment = QActiveDailyMoment.activeDailyMoment;


    @Override
    public List<UserTagRaw> findActiveUserTagsByUserId(int userId){
        log.debug("사용자 태그 목록 조회 - 삭제 되지 않은 일기 대상 조회 (userId={})", userId);
        return queryFactory
                .select(Projections.constructor(UserTagRaw.class,
                        userTag.userTagId,
                        userTag.name,
                        userTag.totalUsageCount
                ))
                .from(userTag)
                .join(momentTag).on(userTag.userTagId.eq(momentTag.userTagId))
                .join(moment).on(momentTag.momentId.eq(moment.momentId))
                .where(moment.userId.eq(userId))
                .distinct()
                .orderBy(userTag.totalUsageCount.desc())
                .fetch();
    }

}
