package com.haruspeak.batch.scheduler;

import com.haruspeak.batch.runner.TodayDiaryJobRunner;
import com.haruspeak.batch.runner.TodayDiaryRetryJobRunner;
import com.haruspeak.batch.runner.TodayThumbnailJobRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryScheduler {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryRetryJobRunner todayDiaryRetryJobRunner;
    private final TodayThumbnailJobRunner todayThumbnailJobRunner;

    /**
     * 매일 자정
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleBidNoticeFetch() {
        String yesterday = LocalDate.now().minusDays(1).format(DATE_FORMAT);
        log.info("🐛 하루 일기 전체 배치 스케줄 시작 - DATE: {}", yesterday);
        Instant start = Instant.now();

        try {
            todayDiaryJobRunner.run(yesterday);
        } catch (Exception e) {
            log.warn("❌ todayDiaryJob 실패 - {}", e.getMessage(), e);
        }

        try {
            todayDiaryRetryJobRunner.run(yesterday);
        } catch (Exception e) {
            log.error("❌ todayDiaryRetryJob 실패 - {}", e.getMessage(), e);
        }

        try {
            todayThumbnailJobRunner.run(yesterday);
        } catch (Exception e) {
            log.error("❌ todayThumbnailJob 실패 - {}", e.getMessage(), e);
        }

        Duration duration = Duration.between(start, Instant.now());
        log.info("✅ 하루 일기 전체 배치 스케줄 종료 - DATE: {}, 소요: {}분({}초)", yesterday, duration.toMinutes(), duration.toSeconds());
    }
}