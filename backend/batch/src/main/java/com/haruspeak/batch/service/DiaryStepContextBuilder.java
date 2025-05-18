package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.*;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryStepContextBuilder {

    private final TodayDiaryDeleteService todayDiaryService;
    private final StepDataStoreService stepDataStoreService;

    public void saveToRedis(List<TodayDiaryContext> diaries) {
        String date = diaries.get(0).getDailySummary().getWriteDate();
        log.debug("🐛 TodayDiary API REDIS 삭제 및 BATCH REDIS 저장 실행 - date= {}", date);

        DiaryStepContext contexts = buildStepContexts(date, diaries);
        stepDataStoreService.storeAll(date, contexts);

        todayDiaryService.deleteSummaryStepData(date, diaries);
    }

    private DiaryStepContext buildStepContexts(String date, List<TodayDiaryContext> diaries) {
        List<ThumbnailGenerateContext> thumbnailContexts = new ArrayList<>();
        List<MomentImageContext> imageContexts = new ArrayList<>();
        List<MomentTagContext> tagContexts = new ArrayList<>();

        for(TodayDiaryContext diary : diaries) {
            DailySummary dailySummary = diary.getDailySummary();

            int userId = dailySummary.getUserId();

            collectThumbnailContext(userId, date, dailySummary, thumbnailContexts);
            collectMomentContexts(userId, diary.getDailyMoments(), tagContexts, imageContexts);

        }

        return new DiaryStepContext(tagContexts, imageContexts, thumbnailContexts);
    }

    private void collectThumbnailContext(
            int userId,
            String date,
            DailySummary summary,
            List<ThumbnailGenerateContext> thumbnailContexts
    ){
        if(summary.getImage() == null) {
            thumbnailContexts.add(buildThumbnailGenerateContext(
                    userId, date, summary.getContent()
            ));
        }
    }

    private void collectMomentContexts(
            int userId,
            List<DailyMoment> moments,
            List<MomentTagContext> tagContexts,
            List<MomentImageContext> imageContexts
    ){
        for (DailyMoment moment : moments) {
            String createdAtForTableJoin = moment.getCreatedAt();

            if (!moment.getTags().isEmpty()) {
                tagContexts.add(buildMomentTagContext(userId, createdAtForTableJoin, moment.getTags()));
            }

            if (!moment.getImages().isEmpty()) {
                imageContexts.add(buildMomentImageContext(userId, createdAtForTableJoin, moment.getImages()));
            }
        }
    }

    private MomentTagContext buildMomentTagContext(int userId, String createdAt, Set<String> tags) {
        return new MomentTagContext(userId, createdAt, tags);
    }

    private MomentImageContext buildMomentImageContext(int userId, String createdAt, Set<String> images) {
        return new MomentImageContext(userId, createdAt, images);
    }

    private ThumbnailGenerateContext buildThumbnailGenerateContext (int userId, String date, String content) {
        return new ThumbnailGenerateContext(userId, date, content);
    }

}
