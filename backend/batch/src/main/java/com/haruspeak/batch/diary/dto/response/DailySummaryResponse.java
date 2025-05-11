package com.haruspeak.batch.diary.dto.response;

import lombok.Data;

public record DailySummaryResponse (
    String title,
    String summary
){
}
