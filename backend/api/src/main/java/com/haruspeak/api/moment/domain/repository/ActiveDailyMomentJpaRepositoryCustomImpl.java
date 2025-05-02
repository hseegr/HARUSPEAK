package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.domain.QMomentImage;
import com.haruspeak.api.moment.domain.QMomentTag;
import com.haruspeak.api.moment.domain.QMomentTagName;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentJpaRepositoryCustomImpl implements ActiveDailyMomentJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MomentDetailRaw> findMomentDetailRaw(Integer userId, Integer momentId) {
        QActiveDailyMoment dailyMoment = QActiveDailyMoment.activeDailyMoment;

        // ActiveDailyMoment
        Tuple moment = queryFactory.select(
                        dailyMoment.momentId,
                        dailyMoment.momentTime,
                        dailyMoment.content
                )
                .from(dailyMoment)
                .where(
                        dailyMoment.userId.eq(userId)
                        .and(dailyMoment.momentId.eq(momentId)
                        )
                )  // userId와 momentId 필터링
                .fetchOne()
                ;

        if (moment == null) {
            return Optional.empty();
        }

        return Optional.of(new MomentDetailRaw(
                moment.get(dailyMoment.momentId),  // momentId
                moment.get(dailyMoment.momentTime),  // momentTime
                getImages(momentId),  // 이미지 URL 리스트
                moment.get(dailyMoment.content),  // content
                getUserTagNames(momentId)  // 태그 리스트
        ));
    }

    @Override
    public List<MomentListDetailRaw> findMomentList(Integer userId, MomentListRequest request) {
        QActiveDailyMoment dailyMoment = QActiveDailyMoment.activeDailyMoment;
        QMomentTag momentTag = QMomentTag.momentTag;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(dailyMoment.userId.eq(userId));

        if (request.before() != null) {
            LocalDateTime beforeDateTime = request.before();
            builder.and(dailyMoment.momentTime.lt(beforeDateTime));
        }

        if (request.startDate() != null && request.endDate() != null) {
            builder.and(dailyMoment.momentTime.between(
                    request.startDate().atStartOfDay(),
                    request.endDate().atTime(23, 59, 59)
            ));
        }

        if (request.userTags() != null && !request.userTags().isEmpty()) {
            builder.and(momentTag.userTagId.in(request.userTags()));
        }

        List<Tuple> moments = queryFactory.select(
                dailyMoment.summaryId,
                dailyMoment.momentId,
                dailyMoment.momentTime,
                dailyMoment.imageCount,
                dailyMoment.content,
                dailyMoment.tagCount
        )
                .from(dailyMoment)
                .join(momentTag).on(dailyMoment.momentId.eq(momentTag.momentId))
                .where(builder)
                .groupBy(dailyMoment.momentId)
                .orderBy(dailyMoment.momentTime.desc())
                .limit(request.limit())
                .fetch()
                ;

        List<MomentListDetailRaw> result = new ArrayList<>();
        for(Tuple moment : moments) {
            int momentId = moment.get(dailyMoment.momentId);
            result.add(new MomentListDetailRaw(
                    moment.get(dailyMoment.summaryId),
                    momentId,
                    moment.get(dailyMoment.momentTime),
                    moment.get(dailyMoment.imageCount),
                    getImages(momentId),
                    moment.get(dailyMoment.content),
                    moment.get(dailyMoment.tagCount),
                    getUserTagNames(momentId)
            ));
        }

        return result;
    }


    private List<String> getImages (Integer momentId){
        QMomentImage momentImage = QMomentImage.momentImage;

        return queryFactory.select(
                momentImage.imageUrl
        )
                .from(momentImage)
                .where(momentImage.momentId.eq(momentId)
                )
                .fetch();
    }

    private List<String> getUserTagNames(Integer momentId){
        QMomentTagName momentTagName = QMomentTagName.momentTagName;

        return queryFactory.select(
                        momentTagName.name
                )
                .from(momentTagName)
                .where(momentTagName.momentId.eq(momentId))
                .fetch()
                ;
    }
}
