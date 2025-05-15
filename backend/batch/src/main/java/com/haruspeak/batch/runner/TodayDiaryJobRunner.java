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
public class TodayDiaryJobRunner {

    private final JobLauncher jobLauncher;
    private final Job todayDiaryJob;
    private final Job retryTodayDiaryJob;
    private final Job todayDiaryTagStepStartJob;
    private final Job todayDiaryImageStepStartJob;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void runTodayDiaryJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}]하루 일기 배치 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayDiaryJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}]하루 일기 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}]하루 일기 배치 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

    public void runTodayDiarySaveJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}]하루 일기 배치(SAVE) 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(retryTodayDiaryJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}]하루 일기 배치(SAVE) 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}]하루 일기 배치(SAVE) 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치(SAVE) 실행 실패 - {}", date, e.getMessage(), e);
        }
    }


    public void runTodayDiaryTagStepStartJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}]하루 일기 Tag step start 배치 부터 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayDiaryTagStepStartJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}]하루 일기 Tag step start 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}]하루 일기 배치 Tag step start 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 Tag step start 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

    public void runTodayDiaryImageStepStartJob(String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}]하루 일기 Image step start 배치 부터 실행 - {}", date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(todayDiaryImageStepStartJob, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}]하루 일기 Image step start 배치 실행 완료 상태 - {}, 소요: {}분({}초)", date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}]하루 일기 배치 Image step start 실행 중 일부 실패 또는 중단: {}", date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}] 하루 일기 배치 Image step start 실행 실패 - {}", date, e.getMessage(), e);
        }
    }

}
