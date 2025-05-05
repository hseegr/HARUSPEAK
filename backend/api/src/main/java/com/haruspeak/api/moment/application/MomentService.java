package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.dto.ResInfo;
import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.exception.common.AccessDeniedException;
import com.haruspeak.api.common.exception.common.InvalidConditionFormatException;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 순간 일기 조회 관련 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MomentService {
    private final ActiveDailyMomentRepository momentRepository;

    /**
     * 일기 상세조회
     * @param userId 사용자 ID
     * @param momentId 일기 ID
     * @return MomentDetailResponse
     */
    @Transactional(readOnly = true)
    public MomentDetailResponse getMomentDetailByMomentId(int userId, int momentId) {
        return momentRepository.findMomentDetail(userId, momentId)
                .map(raw -> {
                    log.debug("✅ 순간 일기 조회 성공 (userId={}, momentId={})", userId, momentId);
                    return toMomentDetail(raw);
                })
                .orElseThrow(() -> {
                    log.warn("⚠️ 조회 실패 - 접근 권한 없거나 존재하지 않음 (userId={}, momentId={})", userId, momentId);
                    return new AccessDeniedException();
                });
    }



    /**
     * 상세 일기 목록 검색
     * @param request 조회 조건
     * @param userId 사용자ID
     * @return MomentListResponse
     */
    @Transactional(readOnly = true)
    public MomentListResponse searchMomentListByCondition(MomentListRequest request, int userId){
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new InvalidConditionFormatException();
            }
        }

        List<MomentDetailRaw> results = momentRepository.findMomentListByCondition(userId,request);
        List<MomentDetailResponse> detailList = mapToMomentDetailResponse(results);

        // 더 조회할 게 남았는지 : 1개 더 조회해서 다 조회 됐으면 hasNext
        boolean hasNext = results.size() > request.getLimit();
        // 커서 : 마지막꺼 ( 다음 조회 시작할 것 )
        LocalDateTime nextCursor = hasNext ? detailList.get(detailList.size() - 1).momentTime() : null;
        // 최종 리스트,
        List<MomentDetailResponse> finalList = detailList.subList(0, Math.min(request.getLimit(), results.size()));

        ResInfo resInfo = new ResInfo(
                finalList.size(),
                nextCursor,
                hasNext
        );

        log.debug("✅ 순간 일기 목록 조회 성공 (userId={}, size={}, hasNext={})", userId, finalList.size(), hasNext);
        return new MomentListResponse(finalList, resInfo);
    }


    /**
     * 특정 일자 상세 일기 목록 불러오기
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return MomentListResponse
     */
    @Transactional(readOnly = true)
    public List<MomentDetailResponse> getMomentListOfSummary(int userId, int summaryId){
        List<MomentDetailRaw> results = momentRepository.findMomentListBySummaryId(userId,summaryId);

        if(results.isEmpty()){
            // summaryId : NOT NULL -> moment : NOT NULL
            log.warn("⚠️ 조회 실패 - 접근 권한 없거나 존재하지 않음 (userId={}, summaryId={})", userId, summaryId);
            throw new AccessDeniedException();
        }

        log.debug("✅ 특정 일자 순간 일기 조회 성공 (userId={}, summaryId={})", userId, summaryId);
        return mapToMomentDetailResponse(results);
    }


    /**
     * from RAW to DTO
     * @param raw 목록 한 줄
     * @return MomentListDetail
     */
    private MomentDetailResponse toMomentDetail(MomentDetailRaw raw) {
        return new MomentDetailResponse(
                raw.summaryId(),
                raw.momentId(),
                raw.momentTime(),
                raw.imageCount(),
                raw.images(),
                raw.content(),
                raw.tagCount(),
                raw.tags()
        );
    }

    /**
     * MomentDetailRaw 목록을 MomentDetailResponse로 변환
     * @param results 목록
     * @return 변환된 목록
     */
    private List<MomentDetailResponse> mapToMomentDetailResponse(List<MomentDetailRaw> results) {
        return results.stream()
                .map(this::toMomentDetail)
                .toList();
    }


}
