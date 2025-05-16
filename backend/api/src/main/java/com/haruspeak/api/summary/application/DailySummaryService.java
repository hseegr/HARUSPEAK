package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.dto.ResInfo;
import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.exception.common.AccessDeniedException;
import com.haruspeak.api.common.exception.common.InvalidConditionFormatException;
import com.haruspeak.api.common.exception.summary.DiaryNotFoundException;
import com.haruspeak.api.moment.application.MomentService;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.MomentListItem;
import com.haruspeak.api.moment.dto.MomentListItemRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.repository.ActiveDailySummaryQdslRepositoryImpl;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.SummaryDetail;
import com.haruspeak.api.summary.dto.SummaryDetailRaw;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.summary.dto.request.DailySummaryUpdateRequest;
import com.haruspeak.api.summary.dto.request.SummaryListRequest;
import com.haruspeak.api.summary.dto.response.DiaryDetailResponse;
import com.haruspeak.api.summary.dto.response.SummaryListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final MomentService momentService;

    private final DailySummaryRepository dailySummaryRepository;
    private final ActiveDailySummaryQdslRepositoryImpl activeSummaryRepository;

    private final ThumnailRegenStateViewer thumnailRegenStateViewer;

    @Transactional
    public void updateDailySummary(Integer summaryId, DailySummaryUpdateRequest request){
        DailySummary dailySummary = dailySummaryRepository.findById(summaryId)
                .orElseThrow(()->new HaruspeakException(ErrorCode.DIARY_NOT_FOUND));

        if(request.title().length()>50){throw new HaruspeakException(ErrorCode.INVALID_TITLE_LENGTH);};
        if(request.content().length()>200){throw new HaruspeakException(ErrorCode.INVALID_CONTENT_LENGTH);};

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
        return new DiaryDetailResponse(getSummaryDetail(userId, summaryId), getMomentListBySummaryId(userId, summaryId));
    }

    /**
     * 하루 일기(요약) 목록 검색
     * @param userId 사용자 ID
     * @param request 검색 조건
     * @return SummaryListResponse
     */
    public SummaryListResponse searchSummaryListByCondition(int userId, SummaryListRequest request){
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new InvalidConditionFormatException();
            }
        }

        List<SummaryDetailRaw> results = activeSummaryRepository.findSummaryListByCondition(userId,request);
        List<SummaryDetail> detailList = mapToSummaryDetail(results, userId);

        // 더 조회할 게 남았는지 : 1개 더 조회해서 다 조회 됐으면 hasNext
        boolean hasNext = results.size() > request.getLimit();
        // 커서 : 마지막꺼 ( 다음 조회 시작할 것 )
        LocalDateTime nextCursor = hasNext ? detailList.get(detailList.size() - 1).diaryDate().atStartOfDay() : null;
        // 최종 리스트,
        List<SummaryDetail> finalList = detailList.subList(0, Math.min(request.getLimit(), results.size()));

        ResInfo resInfo = new ResInfo(
                finalList.size(),
                nextCursor,
                hasNext
        );

        log.debug("✅ 하루 일기 목록 조회 성공 (userId={}, size={}, hasNext={})", userId, finalList.size(), hasNext);
        return new SummaryListResponse(finalList, resInfo);
    }

    /**
     * 일기 삭제
     * @param summaryId
     * @param userId
     */
    @Transactional
    public void deleteSummary(Integer summaryId, Integer userId){
        DailySummary dailySummary = dailySummaryRepository.findById(summaryId)
                .orElseThrow(()->{
                    log.warn("⚠️ 조회 실패 - 존재하지 않음 (userId={}, summaryId={})", userId, summaryId);
                    return new DiaryNotFoundException();}
                );

        if(dailySummary.getUserId() != userId){
            log.warn("⚠️ 조회 실패 - 접근 권한 없음 (userId={}, summaryId={})", userId, summaryId);
            throw new AccessDeniedException();
        }

        if(dailySummary.isDeleted()){
            log.warn("⚠️ 이미 삭제 된 하루 일기 (userId={}, summaryId={})", userId, summaryId);
            throw new HaruspeakException(ErrorCode.DELETED_DIARY);
        }

        try{
            dailySummary.deleteSummary();
        } catch (IllegalArgumentException e) {
            throw new HaruspeakException(ErrorCode.MISSING_REQUIRED_FIELDS, e.getMessage());
        }
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
                    return toSummaryDetail(raw, userId);
                })
                .orElseThrow(() -> {
                    log.warn("⚠️ 조회 실패 - 접근 권한 없거나 존재하지 않음 (userId={}, summaryId={})", userId, summaryId);
                    return new DiaryNotFoundException();
                });
    }

    //private

    /**
     * 하루의 순간 일기 목록 조회
     * @param userId 사용자 ID
     * @param summaryId 하루 일기 ID
     * @return List<MomentDetailResponse>
     */
    private List<MomentListItem> getMomentListBySummaryId(int userId, int summaryId) {
        return momentService.getMomentListOfSummary(userId, summaryId);
    }

    /**
     * from RAW to DTO
     * @param raw 목록 한 줄
     * @return MomentListDetail
     */
    private SummaryDetail toSummaryDetail(SummaryDetailRaw raw, int userId) {
        return new SummaryDetail(
                raw.summaryId(),
                raw.diaryDate(),
                raw.imageUrl(),
                raw.title(),
                raw.content(),
                thumnailRegenStateViewer.isGeneratingOfSummary(userId, raw.summaryId()), // 임시
                raw.imageGenerateCount(),
                raw.contentGenerateCount(),
                raw.momentCount()
        );
    }

    /**
     * MomentDetailRaw 목록을 MomentDetailResponse로 변환
     * @param results 목록
     * @return 변환된 목록
     */
    private List<SummaryDetail> mapToSummaryDetail(List<SummaryDetailRaw> results, int userId) {
        return results.stream()
                .map(result -> toSummaryDetail(result, userId))
                .toList();
    }
}
