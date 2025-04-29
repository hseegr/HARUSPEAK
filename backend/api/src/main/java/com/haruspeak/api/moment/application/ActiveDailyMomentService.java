package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.moment.domain.ActiveDailyMoment;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListDetail;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActiveDailyMomentService {
    private final ActiveDailyMomentRepository activeDailyMomentRepository;

    /**
     * 일기 상세조회
     * @param momentId
     * @return
     */
    @Transactional(readOnly = true)
    public MomentDetailResponse findMomentDetail(Integer momentId){
        MomentDetailRaw raw = activeDailyMomentRepository.getMomentDetailRaw(momentId)
                .orElseThrow(() -> new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND));

        return new MomentDetailResponse(
                raw.momentId(),
                raw.momentTime(),
                splitString(raw.imageUrls()),
                raw.content(),
                splitString(raw.tags())
        );
    }

    /**
     * 상세 일기 목록 불러오기
     * @param request
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public MomentListResponse findMomentList(MomentListRequest request, Integer userId){

        if (request.startDate() != null && request.endDate() != null) {
            if (request.startDate().isAfter(request.endDate())) {
                throw new HaruspeakException(ErrorCode.INVALID_CONDITION_FORMAT);
            }
        }

        List<ActiveDailyMoment> results = activeDailyMomentRepository.getMomentList(userId,request);

        if (results.isEmpty()) {
            throw new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND);
        }

        List<MomentListDetail> detailList = results.stream()
                .map(entity -> new MomentListDetail(
                        entity.getSummaryId(),
                        entity.getMomentId(),
                        entity.getMomentTime(),
                        entity.getImageCount(),
                        splitString(entity.getImageUrls()),
                        entity.getContent(),
                        entity.getTagCount(),
                        splitString(entity.getTags())
                ))
                .collect(Collectors.toList());

        Boolean hasNext = results.size() == request.limit();

        LocalDateTime nextCursor = hasNext ? detailList.get(detailList.size() - 1).momentTime() : null;

        ResInfo resInfo = new ResInfo(
                detailList.size(),
                nextCursor,
                hasNext
        );

        return new MomentListResponse(detailList, resInfo);
    }

    /**
     * String을 리스트로 변경하는 메서드
     * @param source
     * @return
     */
    private List<String> splitString(String source) {
        if (source == null || source.isBlank()) {
            return Collections.emptyList();
        }
        return List.of(source.split(","));
    }
}
