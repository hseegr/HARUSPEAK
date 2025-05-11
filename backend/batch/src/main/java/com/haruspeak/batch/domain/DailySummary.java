package com.haruspeak.batch.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DailySummary {
    private Integer summaryId;
    private int userId;
    private String writeDate;
    private String title;
    private String imageUrl;
    private String content;
    private int imageGenerateCount=1;
    private int contentGenerateCount=1;
    private int momentCount;
    private int viewCount=0;
    private boolean isDeleted;

    public DailySummary (int userId, String writeDate, String title, String imageUrl, String content, int momentCount) {
        this.userId = userId;
        this.writeDate = writeDate;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.momentCount = momentCount;
    }
}
