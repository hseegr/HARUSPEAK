package com.haruspeak.batch.dto.context;

import com.haruspeak.batch.model.DailyMoment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayDiaryTagContext {
    List<DailyMoment> moments;
    Map<String, Integer> tagCountMap;
    int userId;
    String date;
}
