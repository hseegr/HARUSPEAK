package com.haruspeak.batch.domain;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailySummary {
    private Integer summaryId;
    private int userId;
    private LocalDate writeDate;
    private String title;
    private String imageUrl;
    private String content;
    private int imageGenerateCount;
    private int contentGenerateCount;
    private int momentCount;
    private int viewCount;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}
