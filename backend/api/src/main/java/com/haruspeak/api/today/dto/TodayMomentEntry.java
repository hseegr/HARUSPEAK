package com.haruspeak.api.today.dto;

import com.haruspeak.api.today.domain.TodayMoment;

import java.time.LocalDateTime;

public record TodayMomentEntry (
    LocalDateTime createdAt,
    TodayMoment moment
){}
