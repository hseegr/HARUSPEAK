package com.haruspeak.api.summary.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.summary.domain.DailySummary;
import com.haruspeak.api.summary.domain.repository.DailySummaryRepository;
import com.haruspeak.api.summary.dto.request.DailySummaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailySummaryService {
    private final DailySummaryRepository dailySummaryRepository;

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
}
