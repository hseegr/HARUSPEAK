package com.haruspeak.batch.dto.context.result;

import com.haruspeak.batch.dto.context.TodayDiaryContext;

import java.util.List;

public record SummaryProcessingResult(
    List<TodayDiaryContext> successList,
    List<TodayDiaryContext> failedList
){
}
