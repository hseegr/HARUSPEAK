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

@Slf4j
@Component
@NoArgsConstructor
public class TodayImageProcessor implements ItemProcessor <TodayDiary, List<DailyMoment>> {

    @Override
    public List<DailyMoment> process(TodayDiary diary) {
        log.debug("🐛 STEP3.PROCESS - 이미지 ");
        return diary.getDailyMoments();

    }

}
