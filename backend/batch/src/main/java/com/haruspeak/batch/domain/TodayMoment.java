package com.haruspeak.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TodayMoment {
    private String createdAt;
    private String momentTime;
    private String content;
    private List<String> images;
    private List<String> tags;
}
