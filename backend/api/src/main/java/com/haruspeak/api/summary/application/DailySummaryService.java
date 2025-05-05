package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.repository.ActiveSummaryRepository;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.summary.dto.request.DailySummaryUpdateRequest;
import com.haruspeak.api.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySummaryService {
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

    @Transactional(readOnly = true)
    public UserSummaryStat getUserSummaryStat(int userId){
        return activeSummaryRepository.calculateUserSummaryStat(userId);
    }
}
