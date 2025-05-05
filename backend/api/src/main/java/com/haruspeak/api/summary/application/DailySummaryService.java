package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.exception.common.AccessDeniedException;
import com.haruspeak.api.moment.application.MomentService;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.repository.ActiveSummaryRepository;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.SummaryDetail;
import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.summary.dto.request.DailySummaryUpdateRequest;
import com.haruspeak.api.summary.dto.response.DiaryDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final MomentService momentService;

    private final DailySummaryRepository dailySummaryRepository;
    private final ActiveSummaryRepository activeSummaryRepository;

    @Transactional
    public void updateDailySummary(Integer summaryId, DailySummaryUpdateRequest request){
        DailySummary dailySummary = dailySummaryRepository.findById(summaryId)
                .orElseThrow(()->new HaruspeakException(ErrorCode.DIARY_NOT_FOUND));

        try{
            dailySummary.updateSummary(request.title(), request.content());
        } catch (IllegalArgumentException e) {
            throw new HaruspeakException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
    }

    /**
     * 사용자의 일기 스탯 조회
     * @param userId 사용자 ID
     * @return UserSummaryStat
     */
    @Transactional(readOnly = true)
    public UserSummaryStat getUserSummaryStat(int userId){
        return activeSummaryRepository.calculateUserSummaryStat(userId);
    }

    /**
     * 일기 상세 조회
     * @param userId 사용자 ID
     * @param summaryId 일기 ID
     * @return DiaryDetailResponse
     */
    @Transactional(readOnly = true)
    public DiaryDetailResponse getDiaryDetail(int userId, int summaryId){
        return new DiaryDetailResponse(getSummaryDetail(userId, summaryId), getMomentDetails(userId, summaryId));
    }

    /**
     * Summary 상세 정보 조회
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return SummaryDetail
     */
    private SummaryDetail getSummaryDetail(int userId, int summaryId) {
        return activeSummaryRepository.findSummaryDetailRaw(userId, summaryId)
                .map(raw -> {
                    log.debug("✅ 하루 일기 조회 성공 (userId={}, summaryId={})", userId, summaryId);
                    return toSummaryDetail(raw);
                })
                .orElseThrow(() -> {
                    log.warn("⚠️ 조회 실패 - 접근 권한 없거나 존재하지 않음 (userId={}, summaryId={})", userId, summaryId);
                    return new AccessDeniedException(); // 예외를 구체적으로 처리할 수도 있음
                });
    }

    /**
     * 하루의 순간 일기 목록 조회
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return List<MomentDetailResponse>
     */
    private List<MomentDetailResponse> getMomentDetails(int userId, int summaryId) {
        return momentService.getMomentListOfSummary(userId, summaryId);
    }

    /**
     * from RAW to DTO
     * @param raw 목록 한 줄
     * @return MomentListDetail
     */
    private SummaryDetail toSummaryDetail(SummaryDetailRaw raw) {
        return new SummaryDetail(
                raw.summaryId(),
                raw.diaryDate(),
                raw.imageUrl(),
                raw.title(),
                raw.content(),
                raw.imageGenerateCount(),
                raw.contentGenerateCount(),
                raw.momentCount()
        );
    }
}
