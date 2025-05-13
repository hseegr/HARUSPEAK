package com.haruspeak.api.today.domain;

import java.time.LocalDateTime;
import java.util.List;

public record TodayMoment (
    LocalDateTime momentTime,
    String content,
    List<String> tags,
    List<String> images
){}
