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
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentJpaRepositoryCustomImpl implements ActiveDailyMomentJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QActiveDailyMoment moment = QActiveDailyMoment.activeDailyMoment;
    private static final QMomentTag tag = QMomentTag.momentTag;
    private static final QMomentImage image = QMomentImage.momentImage;
    private static final QMomentTagName tagName = QMomentTagName.momentTagName;

    /**
     * 순간 일기 상세 정보 조회
     * @param userId 사용자ID
     * @param momentId 순간일기ID
     * @return MomentDetailRaw
     */
    @Override
    public Optional<MomentDetailRaw> findMomentDetailRaw(Integer userId, Integer momentId) {
        log.debug("✅ 순간 일기 상세 조회 실행 (userId={}, momentId={})", userId, momentId);

        Tuple base = fetchBaseMomentInfo(userId, momentId);
        if (base == null) return Optional.empty();

        return Optional.of(toMomentDetailRaw(base));
    }

    /**
     *
     * @param userId 사용자ID
     * @param request 조건
     * @return List<MomentListDetailRaw>
     */
    @Override
    public List<MomentListDetailRaw> findMomentListDetailRawList(Integer userId, MomentListRequest request) {
        log.debug("✅ 순간 일기 목록 조회 실행 (userId={}, request={})", userId, request);

        BooleanBuilder conditions = buildSearchConditions(userId, request);

        List<Tuple> baseResults = queryFactory.select(
                        moment.summaryId,
                        moment.momentId,
                        moment.momentTime,
                        moment.imageCount,
                        moment.content,
                        moment.tagCount
                )
                .from(moment)
                .leftJoin(tag).on(moment.momentId.eq(tag.momentId)) // inner join tag가 있는 row만 조회하기 위해
                .where(conditions)
                .groupBy(moment.momentId)
                .orderBy(moment.momentTime.desc())
                .limit(request.limit()+1)// 1개 더 조회해서 마지막 tuple로 커서 AND hasMore 체크
                .fetch();

        // 이미지/태그 보강
        return toMomentDetailList(baseResults);
    }


    /**
     * 특정 Moment Tuple 조회
     * @param userId 사용자ID
     * @param momentId 순간일기ID
     * @return ActiveDailyMoment Tuple
     */
    private Tuple fetchBaseMomentInfo(Integer userId, Integer momentId) {
        return queryFactory.select(moment.momentId, moment.momentTime, moment.content)
                .from(moment)
                .where(
                        moment.userId.eq(userId),
                        moment.momentId.eq(momentId)
                )
                .fetchOne();
    }

    /**
     * 튜플 -> DTO로 변환
     * @param tuple Moment row
     * @return MomentDetailRaw
     */
    private MomentDetailRaw toMomentDetailRaw(Tuple tuple) {
        Integer momentId = tuple.get(moment.momentId);
        return new MomentDetailRaw(
                momentId,
                tuple.get(moment.momentTime),
                getImages(momentId),
                tuple.get(moment.content),
                getUserTagNames(momentId)
        );
    }

    /**
     * 튜플 리스트 -> DTO 리스트로 변환
     * @param baseResults Moment 튜플 목록
     * @return List<MomentDetailRaw>
     */
    private List<MomentListDetailRaw> toMomentDetailList(List<Tuple> baseResults) {
        List<Integer> momentIds = baseResults.stream()
                .map(t -> t.get(moment.momentId))
                .toList();

        Map<Integer, List<String>> imageMap = getImagesFor(momentIds);
        Map<Integer, List<String>> tagMap = getTagsFor(momentIds);

        return baseResults.stream()
                .map(tuple -> toMomentListDetailRaw(tuple, imageMap, tagMap))
                .toList();
    }


    /**
     * 튜플 -> DTO로 변환
     * @param tuple Moment row
     * @return MomentDetailRaw
     */
    private MomentListDetailRaw toMomentListDetailRaw(Tuple tuple,
                                                      Map<Integer, List<String>> imageMap,
                                                      Map<Integer, List<String>> tagMap) {
        Integer momentId = tuple.get(moment.momentId);

        return new MomentListDetailRaw(
                tuple.get(moment.summaryId),
                momentId,
                tuple.get(moment.momentTime),
                tuple.get(moment.imageCount),
                imageMap.getOrDefault(momentId, Collections.emptyList()),
                tuple.get(moment.content),
                tuple.get(moment.tagCount),
                tagMap.getOrDefault(momentId, Collections.emptyList())
        );
    }


    /**
     * 검색 조건에 맞춰 Where 구절 생성
     * @param userId 사용자ID
     * @param request 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildSearchConditions(Integer userId, MomentListRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(moment.userId.eq(userId));

        if (request.before() != null) {
            builder.and(moment.momentTime.loe(request.before()));
        }

        if (request.startDate() != null && request.endDate() != null) {
            builder.and(moment.momentTime.between(
                    request.startDate().atStartOfDay(),
                    request.endDate().atTime(23, 59, 59)
            ));
        }

        if (request.userTags() != null && !request.userTags().isEmpty()) {
            builder.and(tag.userTagId.in(request.userTags()));
        }

        return builder;
    }

    /**
      * 이미지주소 불러오기
      * @param momentId 순간일기ID
      * @return List<String> 이미지 주소 목록
      */
    private List<String> getImages(Integer momentId) {
        return queryFactory.select(image.imageUrl)
                .from(image)
                .where(image.momentId.eq(momentId))
                .fetch();
    }

    /**
      * 태그 불러오기
      * @param momentId 순간일기ID
      * @return List<String> 태그 이름 목록
      */
    private List<String> getUserTagNames(Integer momentId) {
        return queryFactory.select(tagName.name)
                .from(tagName)
                .where(tagName.momentId.eq(momentId))
                .fetch();
    }

    /**
     * id별 이미지주소 불러오기
     * @param momentIds id묶음
     * @return id별 이미지주소
     */
    private Map<Integer, List<String>> getImagesFor(List<Integer> momentIds) {
        return queryFactory
                .select(image.momentId, image.imageUrl)
                .from(image)
                .where(image.momentId.in(momentIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(image.momentId),
                        Collectors.mapping(
                                tuple -> tuple.get(image.imageUrl),
                                Collectors.toList()
                        )
                ));
    }

    /**
     * id별 태그 불러오기
     * @param momentIds id묶음
     * @return id별 태그
     */
    private Map<Integer, List<String>> getTagsFor(List<Integer> momentIds) {
        return queryFactory
                .select(tagName.momentId, tagName.name)
                .from(tagName)
                .where(tagName.momentId.in(momentIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(tagName.momentId),
                        Collectors.mapping(
                                tuple -> tuple.get(tagName.name),
                                Collectors.toList()
                        )
                ));
    }


}
