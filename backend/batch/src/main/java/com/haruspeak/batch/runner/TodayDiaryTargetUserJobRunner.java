package com.haruspeak.batch.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryTargetUserJobRunner {

    private final JobLauncher jobLauncher;
    private final Job todayDiaryTargetUserJob;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void run(String userId, String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("userId", userId)
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}(userId:{})]하루 일기 배치 실행 - {}", userId, date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayDiaryTargetUserJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}(userId:{})]하루 일기 배치 실행 완료 상태 - {}, 소요: {}분({}초)", userId, date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}(userId:{})]하루 일기 배치 실행 중 일부 실패 또는 중단: {}", userId, date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}(userId:{})] 하루 일기 배치 실행 실패 - {}", userId, date, e.getMessage(), e);
        }
    }

}
