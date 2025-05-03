package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.exception.user.AccessDeniedException;
import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListDetail;
import com.haruspeak.api.moment.dto.MomentListDetailRaw;
import com.haruspeak.api.moment.dto.ResInfo;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActiveDailyMomentService {
    private final ActiveDailyMomentRepository activeDailyMomentRepository;

    /**
     * 일기 상세조회
     * @param userId 사용자 ID
     * @param momentId 일기 ID
     * @return MomentDetailResponse
     */
    @Transactional(readOnly = true)
    public MomentDetailResponse getMomentDetail(Integer userId, Integer momentId){
        MomentDetailRaw raw = activeDailyMomentRepository.findMomentDetailRaw(userId, momentId)
                .orElseThrow(AccessDeniedException::new);

        return new MomentDetailResponse(
                raw.momentId(),
                raw.momentTime(),
                raw.imageUrls(),
                raw.content(),
                raw.tags()
        );
    }

    /**
     * 상세 일기 목록 불러오기
     * @param request
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public MomentListResponse getMomentList(MomentListRequest request, Integer userId){
        if (request.startDate() != null && request.endDate() != null) {
            if (request.startDate().isAfter(request.endDate())) {
                throw new HaruspeakException(ErrorCode.INVALID_CONDITION_FORMAT);
            }
        }

        List<MomentListDetailRaw> results = activeDailyMomentRepository.findMomentList(userId,request);

        List<MomentListDetail> detailList = results.stream()
                .map(result -> new MomentListDetail(
                        result.summaryId(),
                        result.momentId(),
                        result.momentTime(),
                        result.imageCount(),
                        result.images(),
                        result.content(),
                        result.tagCount(),
                        result.tags()
                ))
                .toList();

        Boolean hasNext = results.size() > request.limit(); // 1개 더 조회해서 다 조회 됐으면 hasNext
        LocalDateTime nextCursor = hasNext ? detailList.get(detailList.size() - 2).momentTime() : null;

        List<MomentListDetail> finalList = detailList.subList(0, Math.min(request.limit(), results.size()));

        ResInfo resInfo = new ResInfo(
                finalList.size(),
                nextCursor,
                hasNext
        );

        return new MomentListResponse(finalList, resInfo);
    }

}
