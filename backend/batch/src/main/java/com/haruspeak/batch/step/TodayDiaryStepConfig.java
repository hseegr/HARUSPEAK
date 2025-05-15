package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.model.MomentImage;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.TodayDiaryTag;
import com.haruspeak.batch.reader.*;
import com.haruspeak.batch.service.redis.*;
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

    private final TodayRedisService todayRedisService;
    private final TodayDiaryRedisKeyService todayDiaryRedisKeyService;
    private final TodayDiaryRedisService todayDiaryRedisService;
    private final TagRedisService tagRedisService;
    private final ImageRedisService imageRedisService;
    private final ThumbnailRedisService thumbnailRedisService;

    private final DailyMomentWriter dailyMomentWriter;
    private final DailySummaryWriter dailySummaryWriter;
    private final MomentTagWriter momentTagWriter;
    private final MomentImageWriter momentImageWriter;
    private final ThumbnailUpdateWriter thumbnailUpdateWriter;



    /**
     * 하루 요약 -> summary, moments insert
     * @return
     */
    @Bean
    public Step todayDiarySaveStep(){
        return new StepBuilder("todayDiarySaveStep", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentReader(null))
                .writer(todayDiaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }


    /**
     * 하루 요약 save -> summary, moments insert
     * @return
     */
    @Bean
    public Step retryTodayDiarySaveStep(){
        return new StepBuilder("retryTodayDiarySaveStep", jobRepository)
                .<TodayDiary, TodayDiary>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .writer(todayDiaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }

    /**
     * 하루 일기 태그
     * @return
     */
    @Bean
    public Step todayTagSaveStep(){
        return new StepBuilder("todayTagSaveStep", jobRepository)
                .<TodayDiaryTag, TodayDiaryTag>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayTagReader(null))
                .writer(momentTagWriter)
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
                .<List<MomentImage>, List<MomentImage>>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayImageReader(null))
                .writer(momentImageWriter)
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
                .<ThumbnailGenerateContext, ThumbnailGenerateContext>chunk(CHUNK_SIZE, transactionManager)
                .reader(thumbnailReader(null))
                .writer(thumbnailUpdateWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .skip(Exception.class)
                .build();
    }


    @Bean
    @StepScope
    public TodayMomentReader todayMomentReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayMomentReader(todayDiaryRedisKeyService, todayRedisService, date);
    }


    @Bean
    @StepScope
    public TodayDiaryReader todayDiaryReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayDiaryReader(todayDiaryRedisService, date);
    }

    @Bean
    @StepScope
    public TodayTagReader todayTagReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayTagReader(tagRedisService, date);
    }

    @Bean
    @StepScope
    public TodayImageReader todayImageReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayImageReader(imageRedisService, date);
    }

    @Bean
    @StepScope
    public ThumbnailReader thumbnailReader(@Value("#{jobParameters['date']}") String date) {
        return new ThumbnailReader(thumbnailRedisService, date);
    }


    @Bean
    public CompositeItemWriter<TodayDiary> todayDiaryWriter() {
        CompositeItemWriter<TodayDiary> writer = new CompositeItemWriter<>();

        List<ItemWriter<? super TodayDiary>> writers = new ArrayList<>();
        writers.add(dailySummaryWriter);
        writers.add(dailyMomentWriter);

        writer.setDelegates(writers);

        return writer;
    }

}
