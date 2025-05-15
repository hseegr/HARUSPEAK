package com.haruspeak.batch.model;

import lombok.*;

@Data
@NoArgsConstructor
public class DailySummary {
    private Integer summaryId;
    private Integer userId;
    private String writeDate;
    private String title;
    private String content;
    private String imageUrl;
    private int momentCount;

    public DailySummary (int userId, String writeDate, String title, String imageUrl, String content, int momentCount) {
        this.userId = userId;
        this.writeDate = writeDate;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.momentCount = momentCount;
    }

    public DailySummary (int userId, String writeDate, int momentCount) {
        this.userId = userId;
        this.writeDate = writeDate;
        this.momentCount = momentCount;
    }

    public void setSummaries(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
