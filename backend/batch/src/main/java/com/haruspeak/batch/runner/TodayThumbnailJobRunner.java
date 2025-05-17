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
public class TodayThumbnailJobRunner {

    private final JobLauncher jobLauncher;
    private final Job todayThumbnailJob;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void run(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 썸네일 배치 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayThumbnailJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 썸네일 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 썸네일 배치 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 썸네일 배치 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

}
