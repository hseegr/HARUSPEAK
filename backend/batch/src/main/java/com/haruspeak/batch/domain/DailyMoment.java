package com.haruspeak.batch.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DailyMoment {
    private int momentId;
    private int summaryId;
    private int userId;
    private String content;
    private LocalDateTime momentTime;
    private int imageCount;
    private int tagCount;
    private int viewCount;
    private LocalDateTime createdAt;
}
