package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.domain.QMomentImage;
import com.haruspeak.api.moment.domain.QMomentTag;
import com.haruspeak.api.moment.domain.QMomentTagName;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListItemRaw;
import com.haruspeak.api.moment.dto.MomentOrder;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActiveDailyMomentQdslRepositoryImpl implements ActiveDailyMomentQdslRepository {

    private final JPAQueryFactory queryFactory;
    private final ActiveDailyMomentJpaRepository jpaRepository;

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
    public Optional<MomentDetailRaw> findActiveMomentDetail(int userId, int momentId) {
        log.debug("순간 일기 상세 조회 실행 (userId={}, momentId={})", userId, momentId);

        Tuple base = fetchBaseActiveMomentInfo(userId, momentId);
        if (base == null) return Optional.empty();

        return Optional.of(toMomentDetailRaw(base));
    }

    /**
     * 조건에 맞는 순간 일기 목록 조회
     * @param userId 사용자 ID
     * @param request 조건
     * @return List<MomentListDetailRaw>
     */
    @Override
    public List<MomentListItemRaw> findActiveMomentListByCondition(int userId, MomentListRequest request) {
        log.debug("순간 일기 목록 검색 실행 (userId={}, request={})", userId, request);

        List<Integer> filteredMomentIds = null;
        if(request.getUserTags() != null && !request.getUserTags().isEmpty()) {
            filteredMomentIds = findMomentIdsMatchingAllUserTags(request.getUserTags());
            if(filteredMomentIds.isEmpty()) return List.of();
        }

        BooleanBuilder conditions = buildSearchConditions(userId, request, filteredMomentIds);
        return toMomentlList(fetchBaseActiveMomentListByCondition(conditions, request.getLimit()));
    }

    /**
     * 특정 일기의 순간 일기 목록 조회
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return List<MomentListDetailRaw>
     */
    @Override
    public List<MomentListItemRaw> findActiveMomentListBySummaryId(int userId, int summaryId) {
        log.debug("순간 일기 목록 조회 실행 (userId={}, summaryId={})", userId, summaryId);
        return toMomentlList(fetchBaseActiveMomentListBySummaryId(userId, summaryId));
    }


    /**
     * select 공통 구문
     * @return List<Expression<?>>
     */
    private List<Expression<?>> selectMomentFields() {
        return List.of(
                moment.summaryId,
                moment.momentId,
                moment.momentTime,
                moment.imageCount,
                moment.content,
                moment.tagCount
        );
    }


    /**
     * 특정 Moment Tuple 조회
     * @param userId 사용자ID
     * @param momentId 순간일기ID
     * @return ActiveDailyMoment Tuple
     */
    private Tuple fetchBaseActiveMomentInfo(Integer userId, Integer momentId) {
        return queryFactory.select(selectMomentFields().toArray(new Expression[0]))
                .from(moment)
                .where(
                        moment.userId.eq(userId),
                        moment.momentId.eq(momentId)
                )
                .fetchOne();
    }

    private List<Tuple> fetchBaseActiveMomentListByCondition(BooleanBuilder conditions, int limit) {
        return queryFactory.select(selectMomentFields().toArray(new Expression[0]))
                .from(moment)
                .leftJoin(tag).on(moment.momentId.eq(tag.momentId))
                .where(conditions)
                .groupBy(moment.momentId)
                .orderBy(moment.momentTime.desc())
                .limit(limit)
                .fetch();
    }

    private List<Tuple> fetchBaseActiveMomentListBySummaryId(int userId, Integer summaryId) {
        return queryFactory.select(selectMomentFields().toArray(new Expression[0]))
                .from(moment)
                .where(
                        moment.userId.eq(userId),
                        moment.summaryId.eq(summaryId)
                )
                .fetch();

    }

    /**
     * 튜플 -> DTO로 변환
     * @param tuple Moment row
     * @return MomentDetailRaw
     */
    private MomentDetailRaw toMomentDetailRaw(Tuple tuple) {
        Integer momentId = tuple.get(moment.momentId);
        return new MomentDetailRaw(
                tuple.get(moment.summaryId),
                momentId,
                tuple.get(moment.momentTime),
                tuple.get(moment.imageCount),
                getImages(momentId),
                tuple.get(moment.content),
                tuple.get(moment.tagCount),
                getUserTagNames(momentId)
        );
    }

    /**
     * 튜플 리스트 -> DTO 리스트로 변환
     * @param baseResults Moment 튜플 목록
     * @return List<MomentDetailRaw>
     */
    private List<MomentListItemRaw> toMomentlList(List<Tuple> baseResults) {
        List<Integer> momentIds = baseResults.stream()
                .map(t -> t.get(moment.momentId))
                .toList();

        Map<Integer, Integer> orderMap = getOrderInDayFor(momentIds);
        Map<Integer, List<String>> imageMap = getImagesFor(momentIds);
        Map<Integer, List<String>> tagMap = getTagsFor(momentIds);

        return baseResults.stream()
                .map(tuple -> toMomentListItemRaw(tuple, imageMap, tagMap, orderMap))
                .toList();
    }


    /**
     * 튜플 -> DTO로 변환
     * @param tuple Moment row
     * @return MomentDetailRaw
     */
    private MomentListItemRaw toMomentListItemRaw(Tuple tuple,
                                                  Map<Integer, List<String>> imageMap,
                                                  Map<Integer, List<String>> tagMap,
                                                  Map<Integer, Integer> orderMap) {
        Integer momentId = tuple.get(moment.momentId);

        return new MomentListItemRaw(
                tuple.get(moment.summaryId),
                momentId,
                orderMap.get(momentId),
                tuple.get(moment.momentTime),
                tuple.get(moment.imageCount),
                imageMap.getOrDefault(momentId, Collections.emptyList()),
                tuple.get(moment.content),
                tuple.get(moment.tagCount),
                tagMap.getOrDefault(momentId, Collections.emptyList())
        );
    }

    /**
     * 검색할 userTags를 모두 포함하는 momentIds 반환
     * - userId 필요없는 이유는 userTagId에 user가 포함되기 때문
     * @param userTagIds 검색 userTags
     * @return 모두 포함하는 moment ids
     */
    private List<Integer> findMomentIdsMatchingAllUserTags(List<Integer> userTagIds) {
        return queryFactory.select(tag.momentId)
                .from(tag)
                .where(tag.userTagId.in(userTagIds))
                .groupBy(tag.momentId)
                .having(tag.userTagId.countDistinct().eq((long) userTagIds.size()))
                .fetch();
    }


    /**
     * 검색 조건에 맞춰 Where 구절 생성
     * @param userId 사용자ID
     * @param condition 조건
     * @param momentIdByTagCondition 태그 검색 조건(MomentIds)
     * @return BooleanBuilder
     */
    private BooleanBuilder buildSearchConditions(Integer userId, MomentListRequest condition, List<Integer> momentIdByTagCondition) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(moment.userId.eq(userId));

        if (condition.getBefore() != null) {
            builder.and(moment.momentTime.loe(condition.getBefore()));
        }

        if (condition.getStartDate() != null && condition.getEndDate() != null) {
            builder.and(moment.momentTime.between(
                    condition.getStartDate().atStartOfDay(),
                    condition.getEndDate().atTime(23, 59, 59)
            ));
        }

        if (momentIdByTagCondition!= null && !momentIdByTagCondition.isEmpty()) {
            builder.and(moment.momentId.in(momentIdByTagCondition));
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


    /**
     * moment가 하루내에 작성된 몇번째 순번인지
     * @param momentIds 순간일기 ids
     * @return momentId 별 order in day
     */
    private Map<Integer, Integer> getOrderInDayFor(List<Integer> momentIds) {
        List<MomentOrder> resultList = jpaRepository.findRanksByMomentIds(momentIds);
        return resultList.stream().collect(Collectors.toMap(
                MomentOrder::getMomentId,
                MomentOrder::getOrderInDay
        ));
    }


}
