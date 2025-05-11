package com.haruspeak.api.summary.domain;

import lombok.Data;

@Data
public class SummaryThumbnailRegenState {
    private String state;
    private String timestamp;
    private int retryCount;
}
