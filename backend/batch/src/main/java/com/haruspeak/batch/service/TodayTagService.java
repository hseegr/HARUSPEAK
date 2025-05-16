package com.haruspeak.batch.service;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.dto.context.TodayDiaryTagContext;
import com.haruspeak.batch.service.redis.TagRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayTagService {

    private final TagRedisService redisService;

    public void saveTagStepData(List<TodayDiaryContext> diaries, String date) {
        log.debug("🐛 태그 STEP DATA REDIS 저장 실행");
        try {
            List<TodayDiaryTagContext> tagStepData = retrieveTagStepData(diaries);
            log.debug("TAG STEP DATA: {}건", tagStepData.size());
            redisService.pushAll(tagStepData, date);
        } catch (Exception e) {
            throw new RuntimeException("💥 태그 STEP DATA REDIS 저장 실패", e);
        }
    }

    private List<TodayDiaryTagContext> retrieveTagStepData(List<TodayDiaryContext> diaries) {
        return diaries.stream()
                .map(this::getDiaryTagIfExists)
                .filter(Objects::nonNull)
                .toList();
    }

    private TodayDiaryTagContext getDiaryTagIfExists(TodayDiaryContext diary) {
        List<DailyMoment> filtered = diary.getDailyMoments().stream()
                .filter(moment -> moment.getTagCount() > 0)
                .toList();

        if (filtered.isEmpty()) return null;

        Map<String, Integer> tagCountMap = calculateTagCounts(filtered);
        return new TodayDiaryTagContext(
                filtered,
                tagCountMap,
                diary.getDailySummary().getUserId(),
                diary.getDailySummary().getWriteDate()
        );
    }

    private Map<String, Integer> calculateTagCounts(List<DailyMoment> moments) {
        log.debug("🐛 [PROCESSOR] 태그별 사용 횟수 정리");
        Map<String, Integer> tagCountMap = new HashMap<>();

        for (DailyMoment moment : moments) {
            for (String tag : moment.getTags()) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        return tagCountMap;
    }
}
