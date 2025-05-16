package com.haruspeak.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodayStepJobConfig {

    private final JobRepository jobRepository;

    private final Step todayDiarySaveStep;
    private final Step todayTagSaveStep;
    private final Step todayImageSaveStep;
    private final Step todayDiaryRetrySaveStep;

    @Bean
    public Job todayDiaryStepJob() {
        return new JobBuilder("todayDiaryStepJob", jobRepository)
                .start(todayDiarySaveStep)
                .build();
    }
    @Bean
    public Job todayTagStepJob() {
        return new JobBuilder("todayTagStepJob", jobRepository)
                .start(todayTagSaveStep)
                .build();
    }
    @Bean
    public Job todayImageStepJob() {
        return new JobBuilder("todayImageStepJob", jobRepository)
                .start(todayImageSaveStep)
                .build();
    }

    @Bean
    public Job todayDiaryRetryStepJob() {
        return new JobBuilder("todayDiaryRetryStepJob", jobRepository)
                .start(todayDiaryRetrySaveStep)
                .build();
    }
}
