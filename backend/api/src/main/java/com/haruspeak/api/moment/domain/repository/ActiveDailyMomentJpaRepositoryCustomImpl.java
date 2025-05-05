package com.haruspeak.api.moment.domain.repository;

import com.haruspeak.api.moment.domain.QActiveDailyMoment;
import com.haruspeak.api.moment.domain.QMomentImage;
import com.haruspeak.api.moment.domain.QMomentTag;
import com.haruspeak.api.moment.domain.QMomentTagName;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
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
    public Optional<MomentDetailRaw> findMomentDetail(int userId, int momentId) {
        log.debug("순간 일기 상세 조회 실행 (userId={}, momentId={})", userId, momentId);

        Tuple base = fetchBaseMomentInfo(userId, momentId);
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
    public List<MomentDetailRaw> findMomentListByCondition(int userId, MomentListRequest request) {
        log.debug("순간 일기 목록 검색 실행 (userId={}, request={})", userId, request);

        BooleanBuilder conditions = buildSearchConditions(userId, request);

        List<Tuple> baseResults = queryFactory.select(selectMomentFields().toArray(new Expression[0]))
                .from(moment)
                .leftJoin(tag).on(moment.momentId.eq(tag.momentId)) // inner join tag가 있는 row만 조회하기 위해
                .where(conditions)
                .groupBy(moment.momentId)
                .orderBy(moment.momentTime.desc())
                .limit(request.getLimit()+1)// 1개 더 조회해서 마지막 tuple로 커서 AND hasMore 체크
                .fetch();

        // 이미지/태그 보강
        return toMomentDetailList(baseResults);
    }

    /**
     * 특정 일기의 순간 일기 목록 조회
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return List<MomentListDetailRaw>
     */
    @Override
    public List<MomentDetailRaw> findMomentListBySummaryId(int userId, int summaryId) {
        log.debug("순간 일기 목록 조회 실행 (userId={}, summaryId={})", userId, summaryId);

        List<Tuple> baseResults = queryFactory.select(selectMomentFields().toArray(new Expression[0]))
                .from(moment)
                .where(
                        moment.userId.eq(userId),
                        moment.summaryId.eq(summaryId)
                )
                .fetch();
        
        return toMomentDetailList(baseResults);
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
    private Tuple fetchBaseMomentInfo(Integer userId, Integer momentId) {
        return queryFactory.select(selectMomentFields().toArray(new Expression[0]))
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
    private List<MomentDetailRaw> toMomentDetailList(List<Tuple> baseResults) {
        List<Integer> momentIds = baseResults.stream()
                .map(t -> t.get(moment.momentId))
                .toList();

        Map<Integer, List<String>> imageMap = getImagesFor(momentIds);
        Map<Integer, List<String>> tagMap = getTagsFor(momentIds);

        return baseResults.stream()
                .map(tuple -> toMomentDetailRaw(tuple, imageMap, tagMap))
                .toList();
    }


    /**
     * 튜플 -> DTO로 변환
     * @param tuple Moment row
     * @return MomentDetailRaw
     */
    private MomentDetailRaw toMomentDetailRaw(Tuple tuple,
                                                      Map<Integer, List<String>> imageMap,
                                                      Map<Integer, List<String>> tagMap) {
        Integer momentId = tuple.get(moment.momentId);

        return new MomentDetailRaw(
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

        if (request.getBefore() != null) {
            builder.and(moment.momentTime.loe(request.getBefore()));
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            builder.and(moment.momentTime.between(
                    request.getStartDate().atStartOfDay(),
                    request.getEndDate().atTime(23, 59, 59)
            ));
        }

        if (request.getUserTags() != null && !request.getUserTags().isEmpty()) {
            builder.and(tag.userTagId.in(request.getUserTags()));
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
