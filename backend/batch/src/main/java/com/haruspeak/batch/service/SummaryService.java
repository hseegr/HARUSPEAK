package com.haruspeak.batch.service;

import com.haruspeak.batch.common.client.fastapi.DailySummaryClient;
import com.haruspeak.batch.common.util.DateFormatUtil;
import com.haruspeak.batch.common.util.TextUtil;
import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.dto.context.result.SummaryProcessingResult;
import com.haruspeak.batch.dto.response.DailySummaryResponse;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final DailySummaryClient dailySummaryClient;

    @Value("${diary.default.thumbnail}")
    private String defaultThumbnail;

    public DailySummaryResponse generateDailySummary(String totalTodayContent) {
      log.debug("🐛 오늘의 제목 및 요약 내용 생성 요청 - {}", totalTodayContent.substring(0, Math.min(10, totalTodayContent.length())));
      try {
          return dailySummaryClient.getDailySummary(totalTodayContent);
      }catch (Exception e) {
          log.error("💥 오늘의 요약 생성 중 에러 발생 - totalTodayCount:{}", totalTodayContent, e);
          throw e;
      }
    }

    public SummaryProcessingResult getTodayDiariesWithSummary(List<TodayDiaryContext> diaries) {
        List<TodayDiaryContext> nonContentList = new ArrayList<>();
        List<TodayDiaryContext> successList = new ArrayList<>();
        List<TodayDiaryContext> failedList = new ArrayList<>();
        diaries.parallelStream().forEach(todayDiary -> {
            try {
                List<DailyMoment> moments = todayDiary.getDailyMoments();
                String totalContent = buildTotalContent(moments);

                if(TextUtil.isMeaningless(totalContent)){
//                    totalContent = buildTotalContentByTags(moments);
//
//                    if(TextUtil.isMeaningless(totalContent)){
                        String date = todayDiary.getDailySummary().getWriteDate();
                        setDefaultDailySummary(todayDiary.getDailySummary(), date);
                        nonContentList.add(todayDiary);
                        log.debug("⚠️ 요약할 내용 없음 - userId:{}",todayDiary.getDailySummary().getUserId());

//                    }else {
//                        DailySummaryResponse summaries = generateDailySummary(totalContent);
//                        setDailySummary(todayDiary.getDailySummary(), summaries);
//                        successList.add(todayDiary);
//                        log.debug("🔎 태그로 요약 생성 userId:{}, {}",todayDiary.getDailySummary().getUserId(), summaries);
//                    }

                } else {
                    DailySummaryResponse summaries = generateDailySummary(totalContent);
                    setDailySummary(todayDiary.getDailySummary(), summaries);
                    successList.add(todayDiary);
                    log.debug("🔎 userId:{}, {}",todayDiary.getDailySummary().getUserId(), summaries);
                }

            }catch (Exception e) {
                log.warn("⚠️ 요약 처리 실패 - userId: {}, date: {}", todayDiary.getDailySummary().getUserId(), todayDiary.getDailySummary().getWriteDate(), e);
                failedList.add(new TodayDiaryContext(todayDiary.getDailySummary(), todayDiary.getDailyMoments()));
                todayDiary.setDailySummary(null);
            }
        });

        log.info("✅ 요약 생성 성공 {}건, 기본 적용 {}건, 실패 {}건", successList.size(), nonContentList.size(), failedList.size());
        return new SummaryProcessingResult(nonContentList, successList, failedList);
    }


    private String buildTotalContent(List<DailyMoment> moments) {
        String result = moments.stream()
                .map(DailyMoment::getContent)
                .filter(content -> !TextUtil.isMeaningless(content))
                .collect(Collectors.joining("\n\n"));

        return TextUtil.isMeaningless(result) ? null : result;
    }


    private String buildTotalContentByTags(List<DailyMoment> moments) {
        String result = moments.stream()
                .map(moment -> {
                    Set<String> tags = moment.getTags();
                    if (tags == null || tags.isEmpty()) return null;

                    String line = String.join(" ", tags);
                    return TextUtil.isMeaningless(line) ? null : line;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        return TextUtil.isMeaningless(result) ? null : result;
    }

    private void setDailySummary(DailySummary dailySummary, DailySummaryResponse summaries) {
        dailySummary.setSummaries(summaries.title(), summaries.summary());
    }

    private void setDefaultDailySummary(DailySummary dailySummary, String date) {
        String krDate = DateFormatUtil.formatToKoreanDate(date);
        String defaultTitle = String.format("%s의 일기", krDate);
        dailySummary.setSummariesAndImage(defaultThumbnail, defaultTitle, "");
    }

}
