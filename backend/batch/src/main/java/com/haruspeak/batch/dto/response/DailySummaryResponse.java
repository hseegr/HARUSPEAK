package com.haruspeak.batch.dto.response;

import lombok.Data;

public record DailySummaryResponse (
    String title,
    String summary
){
}
