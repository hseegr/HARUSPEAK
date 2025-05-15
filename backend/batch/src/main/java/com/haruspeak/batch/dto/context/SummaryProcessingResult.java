package com.haruspeak.batch.dto.context;

import com.haruspeak.batch.model.TodayDiary;

import java.util.List;

public record SummaryProcessingResult(
    List<TodayDiary> successList,
    List<TodayDiary> failedList
){
}
