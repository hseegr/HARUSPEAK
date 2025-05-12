package com.haruspeak.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TodayDiary {
    private DailySummary dailySummary;
    private List<DailyMoment> dailyMoments;
}
