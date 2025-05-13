package com.haruspeak.batch.scheduler;

import com.haruspeak.batch.runner.TodayDiaryJobRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryScheduler {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TodayDiaryJobRunner todayDiaryJobRunner;

    /**
     * 매일 자정
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleBidNoticeFetch() {
        String yesterday = LocalDate.now().minusDays(1).format(DATE_FORMAT);
        log.info("🐛 하루 일기 배치 스케줄 시작 - DATE: {}", yesterday);
        try {
            todayDiaryJobRunner.runTodayDiaryJob(yesterday);
            log.info("🐛 하루 일기 배치 스케줄 종료 - DATE: {}", yesterday);
        } catch (Exception e) {
            log.error("🐛 하루 일기 배치 스케줄 실패 - DATE: {} ({})", yesterday, e.getMessage(), e);
        }
    }
}