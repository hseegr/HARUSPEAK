package com.haruspeak.batch.processor;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.TodayDiaryTag;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class TodayImageProcessor implements ItemProcessor <TodayDiary, List<DailyMoment>> {

    @Override
    public List<DailyMoment> process(TodayDiary diary) {
        log.debug("🐛 [PROCESSOR] 이미지가 포함된 순간 일기 필터링");
        return getMomentsWithNonZeroImages(diary.getDailyMoments());

    }

    private List<DailyMoment> getMomentsWithNonZeroImages(List<DailyMoment> moments) {
        return moments.stream()
                .filter(moment -> moment.getImageCount() > 0)
                .toList();
    }

}
