package com.haruspeak.batch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private Set<String> images;
    private Set<String> tags;
    private Set<Integer> userTagIds;
}
