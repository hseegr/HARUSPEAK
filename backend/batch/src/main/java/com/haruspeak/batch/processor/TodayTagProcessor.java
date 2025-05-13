package com.haruspeak.batch.processor;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.TodayDiaryTag;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor
public class TodayTagProcessor implements ItemProcessor <TodayDiary, TodayDiaryTag> {

    @Override
    public TodayDiaryTag process(TodayDiary diary) {
        log.debug("🐛 STEP2.PROCESS - 태그별 사용 횟수 정리");

        Map<String, Integer> tagCountMap = calculateTagCounts(diary.getDailyMoments());
        return new TodayDiaryTag(diary.getDailyMoments(), tagCountMap, diary.getDailySummary().getUserId(), diary.getDailySummary().getWriteDate());

    }

    private Map<String, Integer> calculateTagCounts(List<DailyMoment> moments) {
        Map<String, Integer> tagCountMap = new HashMap<>();

        for (DailyMoment moment : moments) {
            for (String tag : moment.getTags()) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        return tagCountMap;
    }
}
