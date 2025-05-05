package com.haruspeak.api.user.application;

import com.haruspeak.api.moment.application.TodayService;
import com.haruspeak.api.summary.application.DailySummaryService;
import com.haruspeak.api.summary.dto.UserSummaryStat;
import com.haruspeak.api.user.dto.response.UserStatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatService {

    private final TodayService todayService;
    private final DailySummaryService summaryService;

    /**
     * 사용자 스탯 조회
     * @param userId 사용자 ID
     * @return UserStatResponse (moment count, summary count, today moment count)
     */
    public UserStatResponse getUserStat(int userId) {
        long todayCount = todayService.getTodayMomentCount(userId, LocalDate.now());
        UserSummaryStat summaryStat = summaryService.getUserSummaryStat(userId);

        log.debug("✅ 오늘 작성한 moment 수 = {}, 전체 통계 = {}", todayCount, summaryStat);

        return new UserStatResponse(
                todayCount,
                summaryStat.totalDiaryCount(),
                summaryStat.totalMomentCount()
        );
    }


}
