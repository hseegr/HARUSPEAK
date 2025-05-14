package com.haruspeak.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayDiary {
    private DailySummary dailySummary;
    private List<DailyMoment> dailyMoments;
}
