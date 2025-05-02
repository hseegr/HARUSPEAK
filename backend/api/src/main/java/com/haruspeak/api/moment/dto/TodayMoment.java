package com.haruspeak.api.moment.dto;

import java.util.List;

public record TodayMoment(
        String createdAt,
        String momentTime,
        List<String> images,
        String content,
        List<String> tags
) {
}
