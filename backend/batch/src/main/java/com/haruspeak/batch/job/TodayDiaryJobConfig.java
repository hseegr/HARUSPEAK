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
public class TodayDiaryJobConfig {

    private final JobRepository jobRepository;

    private final Step todayDiarySaveStep;
    private final Step todayDiarySaveByUserStep;
    private final Step todayTagSaveStep;
    private final Step todayImageSaveStep;
    private final Step todayThumbnailUpdateStep;

    @Bean
    public Job todayDiaryJob() {
        return new JobBuilder("todayDiaryJob", jobRepository)
                .start(todayDiarySaveStep)
                .next(todayTagSaveStep)
                .next(todayImageSaveStep)
                .next(todayThumbnailUpdateStep)
                .build();
    }

    @Bean
    public Job todayDiarySaveJob() {
        return new JobBuilder("todayDiarySaveJob", jobRepository)
                .start(todayDiarySaveByUserStep)
                .next(todayTagSaveStep)
                .next(todayImageSaveStep)
                .next(todayThumbnailUpdateStep)
                .build();
    }

    @Bean
    public Job todayDiaryTagStepStartJob() {
        return new JobBuilder("todayDiaryTagStepStartJob", jobRepository)
                .start(todayTagSaveStep)
                .next(todayImageSaveStep)
                .next(todayThumbnailUpdateStep)
                .build();
    }

    @Bean
    public Job todayDiaryImageStepStartJob() {
        return new JobBuilder("todayDiaryImageStepStartJob", jobRepository)
                .start(todayImageSaveStep)
                .next(todayThumbnailUpdateStep)
                .build();
    }

    @Bean
    public Job dailySummaryStepJob() {
        return new JobBuilder("dailySummaryStepJob", jobRepository)
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
    public Job todayThumbnailStepJob() {
        return new JobBuilder("todayThumbnailStepJob", jobRepository)
                .start(todayThumbnailUpdateStep)
                .build();
    }

    @Bean
    public Job dailySummaryStepByUserJob() {
        return new JobBuilder("dailySummaryStepByUserJob", jobRepository)
                .start(todayDiarySaveByUserStep)
                .build();
    }


}
