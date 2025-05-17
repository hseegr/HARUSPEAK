package com.haruspeak.batch.dto.context;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayDiaryContext {
    private DailySummary dailySummary;
    private List<DailyMoment> dailyMoments;
}
