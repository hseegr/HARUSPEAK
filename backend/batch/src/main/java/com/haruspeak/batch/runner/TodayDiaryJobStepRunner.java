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

/**
 * 입찰공고 배치 작업을 실행하기 위한 클래스
 * 컨트롤러나 스케쥴러 등에서 호출하여 배치 작업을 실행
 * JobLauncher를 통해 Job을 실행하고, JobParameters를 구성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TodayDiaryJobStepRunner {

    private final JobLauncher jobLauncher;
    private final Job dailySummaryStepJob;
    private final Job todayTagStepJob;
    private final Job todayImageStepJob;
    private final Job todayThumbnailStepJob;
    private final Job dailySummaryStepByUserJob;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void runDailySummaryStepJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 하루 일기 배치 Summary STEP 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(dailySummaryStepJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 하루 일기 배치 Summary STEP 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 하루 일기 배치 Summary STEP 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 실행 실패 - {}", date, e.getMessage(), e);
        }
    }


    public void runTodayDiaryTagStepJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 하루 일기 Tag STEP 배치 부터 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayTagStepJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 하루 일기 Tag STEP 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 하루 일기 배치 Tag STEP 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 Tag STEP 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

    public void runTodayDiaryImageStepJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 하루 일기 Image STEP 배치 부터 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayImageStepJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 하루 일기 Image STEP 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 하루 일기 배치 Image STEP 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 Image STEP 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

    public void runTodayThumbnailStepJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 하루 일기 배치 Thumbnail STEP 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayThumbnailStepJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 하루 일기 배치 Thumbnail STEP 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 하루 일기 배치 Thumbnail STEP 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 실행 실패 - {}", date, e.getMessage(), e);
        }
    }


    public void runDailySummaryStepByUserJob(String date, String userId) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addString("userId", userId)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}] 특정 유저 하루 일기 SUMMARY STEP 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(dailySummaryStepByUserJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}] 특정 유저 하루 일기 SUMMARY STEP 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}] 특정 유저 하루 일기 SUMMARY STEP 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 특정 유저 하루 일기 SUMMARY STEP 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

}
