package com.haruspeak.batch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMoment {
    private Integer userId;
    private String momentTime;
    private String content;
    private String createdAt;
    private int imageCount;
    private int tagCount;
    private List<String> images;
    private List<String> tags;
    private List<Integer> userTagIds;
}
