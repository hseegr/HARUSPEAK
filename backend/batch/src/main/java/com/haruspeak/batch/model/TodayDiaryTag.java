package com.haruspeak.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class TodayDiaryTag {
    List<DailyMoment> moments;
    Map<String, Integer> tagCountMap;
    int userId;
    String date;
}
