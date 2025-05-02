package com.haruspeak.api.diary.dto.request;

public record DailySummaryUpdateRequest(
        String title,
        String content
) {
}
