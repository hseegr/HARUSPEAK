package com.haruspeak.batch.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DailyMoment {
    private String summaryId;
    private String momentTime;
    private String content;
    private String createdAt;
    private int imageCount;
    private int tagCount;
    private List<String> images;
    private List<String> tags;
    private List<Integer> userTagIds;
}
