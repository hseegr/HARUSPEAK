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
public class TodayStepJobRunner {

    private final JobLauncher jobLauncher;
    private final Job todayDiaryStepJob;
    private final Job todayTagStepJob;
    private final Job todayImageStepJob;
    private final Job todayDiaryRetryStepJob;


    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private void run(Job job, String date) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("🐛 [{}: {}] 하루 일기 스텝 배치 실행 - {}", job.getName(), date, LocalDateTime.now().format(TIME_FORMATTER));
            Instant start = Instant.now();

            JobExecution execution = jobLauncher.run(job, jobParameters);
            Duration duration = Duration.between(start, Instant.now());

            log.info("🐛 [{}: {}] 하루 일기 스텝 배치 실행 완료 상태 - {}, 소요: {}분({}초)", job.getName(), date, execution.getStatus(), duration.toMinutes(), duration.toSeconds());

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                log.warn("🐛 [{}: {}] 하루 일기 스텝 배치 실행 중 일부 실패 또는 중단: {}", job.getName(), date,  execution.getExitStatus());
            }

        } catch (Exception e) {
            log.error("🐛 [{}: {}] 하루 일기 스텝 배치 실행 실패 - {}", job.getName(), date, e.getMessage(), e);
        }
    }

    public void runTodayDiaryStepJob(String date){
        run(todayDiaryStepJob, date);
    }

    public void runTodayTagStepJob(String date){
        run(todayTagStepJob, date);
    }

    public void runTodayImageStepJob(String date){
        run(todayImageStepJob, date);
    }

    public void runTodayDiaryRetryStepJob(String date){
        run(todayDiaryRetryStepJob, date);
    }
}

