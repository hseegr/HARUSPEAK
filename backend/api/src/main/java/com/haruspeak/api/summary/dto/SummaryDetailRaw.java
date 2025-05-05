package com.haruspeak.api.summary.dto;

import java.time.LocalDate;

public record SummaryDetailRaw (
        int summaryId,
        LocalDate diaryDate,
        String imageUrl,
        String title,
        String content,
        int imageGenerateCount,
        int contentGenerateCount,
        int momentCount
){
}
