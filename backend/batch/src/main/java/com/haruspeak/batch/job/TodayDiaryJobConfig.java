package com.haruspeak.batch.job;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.TodayDiaryTag;
import com.haruspeak.batch.model.repository.*;
import com.haruspeak.batch.processor.TodayImageProcessor;
import com.haruspeak.batch.processor.TodaySummaryProcessor;
import com.haruspeak.batch.processor.TodayTagProcessor;
import com.haruspeak.batch.reader.TodayDiaryReader;
import com.haruspeak.batch.reader.TodayMomentReader;
import com.haruspeak.batch.service.TodayDiaryService;
import com.haruspeak.batch.service.TodaySummaryService;
import com.haruspeak.batch.writer.DailySummaryWriter;
import com.haruspeak.batch.writer.TodayImageWriter;
import com.haruspeak.batch.writer.TodayTagWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

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
    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;
    private final MomentTagRepository momentTagRepository;

    private final MomentImageRepository momentImageRepository;

    private final TodaySummaryService todaySummaryService;
    private final TodayDiaryService todayDiaryService;

    @Bean
    public Job todayDiaryJob() {
        return new JobBuilder("todayDiaryJob", jobRepository)
                .start(dailySummaryStep())
                .next(dailyTagStep())
                .next(dailyImageStep())
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
                .reader(todayMomentReader())
                .processor(todaySummaryProcessor())
                .writer(dailySummaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 일기 태그
     * @return
     */
    @Bean
    public Step dailyTagStep(){
        return new StepBuilder("dailyTagStep", jobRepository)
                .<TodayDiary, TodayDiaryTag>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader())
                .processor(todayTagProcessor())
                .writer(todayTagWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 일기 이미지
     * @return
     */
    @Bean
    public Step dailyImageStep(){
        return new StepBuilder("dailyImageStep", jobRepository)
                .<TodayDiary, List<DailyMoment>>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader())
                .processor(todayImageProcessor())
                .writer(todayImageWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<TodayDiary> todayMomentReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayMomentReader(todayRedisRepository, date);
    }

    @Bean
    @StepScope
    public ItemProcessor<TodayDiary, TodayDiary> todaySummaryProcessor() {
        return new TodaySummaryProcessor(todaySummaryService);
    }

    @Bean
    @StepScope
    public ItemWriter<TodayDiary> dailySummaryWriter() {
        return new DailySummaryWriter(dailySummaryRepository, dailyMomentRepository, todayDiaryService);
    }

    @Bean
    @StepScope
    public ItemReader<TodayDiary> todayDiaryReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayDiaryReader(todayDiaryRedisRepository, date);
    }

    @Bean
    @StepScope
    public ItemProcessor <TodayDiary, TodayDiaryTag>  todayTagProcessor() {
        return new TodayTagProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<TodayDiaryTag> todayTagWriter() {
        return new TodayTagWriter(tagRepository, userTagRepository, momentTagRepository);
    }


    @Bean
    @StepScope
    public TodayImageProcessor todayImageProcessor() {
        return new TodayImageProcessor();
    }

    @Bean
    @StepScope
    public TodayImageWriter todayImageWriter() {
        return new TodayImageWriter(momentImageRepository);
    }
}
