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
public class TodayDiaryTargetUserJobConfig {

    private final JobRepository jobRepository;

    private final Step todayDiaryTargetUserSaveStep;
    private final Step todayTagSaveStep;
    private final Step todayImageSaveStep;


    @Bean
    public Job todayDiaryTargetUserJob() {
        return new JobBuilder("todayDiaryTargetUserJob", jobRepository)
                .start(todayDiaryTargetUserSaveStep)
                .next(todayTagSaveStep)
                .next(todayImageSaveStep)
                .build();
    }

}
