package com.haruspeak.batch.model;

import lombok.*;

@Data
@NoArgsConstructor
public class DailySummary {
    private Integer userId;
    private String writeDate;
    private String title;
    private String content;
    private int momentCount;

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
