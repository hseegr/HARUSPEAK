package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.exception.user.AccessDeniedException;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentListDetail;
import com.haruspeak.api.moment.dto.MomentListDetailRaw;
import com.haruspeak.api.moment.dto.ResInfo;
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
    public MomentDetailResponse getMomentDetail(Integer userId, Integer momentId) {
        return momentRepository.findMomentDetailRaw(userId, momentId)
                .map(raw -> {
                    log.debug("✅ 순간 일기 조회 성공 (momentId={}, userId={})", momentId, userId);
                    return new MomentDetailResponse(
                            raw.momentId(),
                            raw.momentTime(),
                            raw.imageUrls(),
                            raw.content(),
                            raw.tags()
                    );
                })
                .orElseThrow(() -> {
                    log.warn("⚠️ 조회 실패 - 접근 권한 없거나 존재하지 않음 (momentId={}, userId={})", momentId, userId);
                    return new AccessDeniedException();
                });
    }



    /**
     * 상세 일기 목록 불러오기
     * @param request 조회 조건
     * @param userId 사용자ID
     * @return MomentListResponse
     */
    @Transactional(readOnly = true)
    public MomentListResponse getMomentList(MomentListRequest request, Integer userId){
        if (request.startDate() != null && request.endDate() != null) {
            if (request.startDate().isAfter(request.endDate())) {
                throw new HaruspeakException(ErrorCode.INVALID_CONDITION_FORMAT);
            }
        }

        List<MomentListDetailRaw> results = momentRepository.findMomentList(userId,request);

        List<MomentListDetail> detailList = results.stream()
                .map(this::toMomentListDetail)
                .toList();


        // 더 조회할 게 남았는지 : 1개 더 조회해서 다 조회 됐으면 hasNext
        boolean hasNext = results.size() > request.limit();
        // 커서 : 마지막꺼 ( 다음 조회 시작할 것 )
        LocalDateTime nextCursor = hasNext ? detailList.get(detailList.size() - 1).momentTime() : null;
        // 최종 리스트,
        List<MomentListDetail> finalList = detailList.subList(0, Math.min(request.limit(), results.size()));

        ResInfo resInfo = new ResInfo(
                finalList.size(),
                nextCursor,
                hasNext
        );

        log.debug("✅ 순간 일기 목록 조회 성공 (userId={}, size={}, hasNext={})", userId, finalList.size(), hasNext);
        return new MomentListResponse(finalList, resInfo);
    }

    /**
     * from RAW to DTO
     * @param raw 목록 한 줄
     * @return MomentListDetail
     */
    private MomentListDetail toMomentListDetail(MomentListDetailRaw raw) {
        return new MomentListDetail(
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


}
