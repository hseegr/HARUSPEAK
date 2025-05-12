package com.haruspeak.batch.job;

import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.DailyMomentRepository;
import com.haruspeak.batch.model.repository.DailySummaryRepository;
import com.haruspeak.batch.model.repository.TodayDiaryRedisRepository;
import com.haruspeak.batch.model.repository.TodayRedisRepository;
import com.haruspeak.batch.processor.TodaySummaryProcessor;
import com.haruspeak.batch.reader.TodayMomentReader;
import com.haruspeak.batch.service.TodaySummaryService;
import com.haruspeak.batch.writer.DailySummaryWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodayDiaryJobConfig {

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final DailySummaryRepository dailySummaryRepository;
    private final DailyMomentRepository dailyMomentRepository;

    private final TodayRedisRepository todayRedisRepository;
    private final TodayDiaryRedisRepository todayDiaryRedisRepository;

    TodaySummaryService todaySummaryService;

    @Bean
    public Job todayDiaryJob() {
        return new JobBuilder("todayDiaryJob", jobRepository)
                .start(dailySummaryStep())
                .build();
    }

    /**
     * 하루 요약 -> summary, moments insert
     * @return
     */
    @Bean
    public Step dailySummaryStep(){
        return new StepBuilder("dailySummaryStep", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentReader(null))
                .processor(todaySummaryProcessor(null))
                .writer(dailySummaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    @StepScope
    public TodayMomentReader todayMomentReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayMomentReader(todayRedisRepository, date);
    }

    @Bean
    @StepScope
    public TodaySummaryProcessor todaySummaryProcessor(@Value("#{jobParameters['date']}") String date) {
        return new TodaySummaryProcessor(todaySummaryService, date);
    }

    @Bean
    @StepScope
    public DailySummaryWriter dailySummaryWriter() {
        return new DailySummaryWriter(dailySummaryRepository, dailyMomentRepository, todayRedisRepository, todayDiaryRedisRepository);
    }

}
