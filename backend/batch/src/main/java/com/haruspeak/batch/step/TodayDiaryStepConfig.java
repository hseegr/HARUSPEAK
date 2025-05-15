package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.ThumbnailUpdateDTO;
import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.TodayDiaryTag;
import com.haruspeak.batch.model.repository.*;
import com.haruspeak.batch.processor.TodayImageProcessor;
import com.haruspeak.batch.processor.TodaySummaryProcessor;
import com.haruspeak.batch.processor.TodayTagProcessor;
import com.haruspeak.batch.processor.TodayThumbnailProcessor;
import com.haruspeak.batch.reader.TodayDiaryReader;
import com.haruspeak.batch.reader.TodayMomentByUserReader;
import com.haruspeak.batch.reader.TodayMomentReader;
import com.haruspeak.batch.service.TodayDiaryService;
import com.haruspeak.batch.service.TodaySummaryService;
import com.haruspeak.batch.service.TodayThumbnailService;
import com.haruspeak.batch.writer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodayDiaryStepConfig {
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
    private final TodayThumbnailService todayThumbnailService;
    private final TodayDiaryService todayDiaryService;


    /**
     * 하루 요약 -> summary, moments insert
     * @return
     */
    @Bean
    public Step todayDiarySaveStep(){
        return new StepBuilder("todayDiarySaveStep", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentReader(null))
                .processor(todaySummaryProcessor())
                .writer(todayDiaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }



    /**
     * 하루 요약 save -> summary, moments insert
     * @return
     */
    @Bean
    public Step todayDiarySaveByUserStep(){
        return new StepBuilder("todayDiarySaveByUserStep", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .processor(todaySummaryProcessor())
                .writer(dailySummaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 일기 태그
     * @return
     */
    @Bean
    public Step todayTagSaveStep(){
        return new StepBuilder("todayTagSaveStep", jobRepository)
                .<TodayDiary, TodayDiaryTag>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .processor(todayTagProcessor())
                .writer(momentTagWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 일기 이미지
     * @return
     */
    @Bean
    public Step todayImageSaveStep(){
        return new StepBuilder("todayImageSaveStep", jobRepository)
                .<TodayDiary, List<DailyMoment>>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .processor(todayImageProcessor())
                .writer(momentImageWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 썸네일 업데이트
     * @return
     */
    @Bean
    public Step todayThumbnailUpdateStep(){
        return new StepBuilder("todayThumbnailUpdateStep", jobRepository)
                .<TodayDiary, ThumbnailUpdateDTO >chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .processor(todayThumbnailProcessor())
                .writer(todayThumbnailWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }

    /**
     * 하루 요약 -> summary, moments insert
     * @return
     */
    @Bean
    public Step dailySummaryStepByUser(){
        return new StepBuilder("dailySummaryStepByUser", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentByUserReader(null, null))
                .processor(todaySummaryProcessor())
                .writer(dailySummaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public TodayMomentReader todayMomentReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayMomentReader(todayRedisRepository, date);
    }

    @Bean
    @StepScope
    public TodayMomentByUserReader todayMomentByUserReader(
            @Value("#{jobParameters['date']}") String date,
            @Value("#{jobParameters['userId']}") String userId
    ) {
        return new TodayMomentByUserReader(todayRedisRepository, date, userId, false);
    }

    @Bean
    @StepScope
    public TodaySummaryProcessor todaySummaryProcessor() {
        return new TodaySummaryProcessor(todaySummaryService);
    }

    @Bean
    @StepScope
    public DailyMomentWriter dailyMomentWriter() {
        return new DailyMomentWriter(dailyMomentRepository, todayDiaryService);
    }

    @Bean
    @StepScope
    public DailySummaryWriter dailySummaryWriter() {
        return new DailySummaryWriter(dailySummaryRepository);
    }

    @Bean
    @StepScope
    public TodayDiaryReader todayDiaryReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayDiaryReader(todayDiaryRedisRepository, date);
    }

    @Bean
    @StepScope
    public TodayTagProcessor todayTagProcessor() {
        return new TodayTagProcessor();
    }

    @Bean
    @StepScope
    public MomentTagWriter momentTagWriter() {
        return new MomentTagWriter(tagRepository, userTagRepository, momentTagRepository);
    }


    @Bean
    @StepScope
    public TodayImageProcessor todayImageProcessor() {
        return new TodayImageProcessor();
    }

    @Bean
    @StepScope
    public MomentImageWriter momentImageWriter() {
        return new MomentImageWriter(momentImageRepository);
    }

    @Bean
    @StepScope
    public TodayThumbnailProcessor todayThumbnailProcessor() {
        return new TodayThumbnailProcessor(todayThumbnailService);
    }

    @Bean
    @StepScope
    public TodayThumbnailWriter todayThumbnailWriter() {
        return new TodayThumbnailWriter(dailySummaryRepository);
    }

    @Bean
    public CompositeItemWriter<TodayDiary> todayDiaryWriter() {
        CompositeItemWriter<TodayDiary> writer = new CompositeItemWriter<>();

        List<ItemWriter<? super TodayDiary>> writers = new ArrayList<>();
        writers.add(dailySummaryWriter());
        writers.add(dailyMomentWriter());

        writer.setDelegates(writers);

        return writer;
    }

}
