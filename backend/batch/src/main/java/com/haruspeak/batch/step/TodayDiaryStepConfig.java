package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.context.TodayDiaryContext;
import com.haruspeak.batch.listener.SummaryStepListener;
import com.haruspeak.batch.reader.TodayDiaryReader;
import com.haruspeak.batch.reader.TodayMomentReader;
import com.haruspeak.batch.reader.TodayMomentTargetUserReader;
import com.haruspeak.batch.service.redis.*;
import com.haruspeak.batch.writer.DailyMomentWriter;
import com.haruspeak.batch.writer.DailySummaryWriter;
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

    private final SummaryStepListener summaryStepListener;

    private final TodayRedisService todayRedisService;
    private final TodayDiaryRedisKeyService todayDiaryRedisKeyService;
    private final TodayDiaryRedisService todayDiaryRedisService;

    private final DailyMomentWriter dailyMomentWriter;
    private final DailySummaryWriter dailySummaryWriter;



    /**
     * 하루 요약 -> summary, moments insert
     * @return
     */
    @Bean
    public Step todayDiarySaveStep(){
        return new StepBuilder("todayDiarySaveStep", jobRepository)
                .<TodayDiaryContext, TodayDiaryContext>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentReader(null))
                .writer(todayDiaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .listener(summaryStepListener)
                .build();
    }

    /**
     * 특정유저에 대해서 진행.
     * @return
     */
    @Bean
    public Step todayDiaryTargetUserSaveStep(){
        return new StepBuilder("todayDiaryTargetUserSaveStep", jobRepository)
                .<TodayDiaryContext, TodayDiaryContext>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayMomentTargetUserReader(null, null))
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
    public Step todayDiaryRetrySaveStep(){
        return new StepBuilder("todayDiaryRetrySaveStep", jobRepository)
                .<TodayDiaryContext, TodayDiaryContext>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayDiaryReader(null))
                .writer(todayDiaryWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }


    @Bean
    @StepScope
    public TodayMomentReader todayMomentReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayMomentReader(todayDiaryRedisKeyService, todayRedisService, date);
    }

    @Bean
    @StepScope
    public TodayMomentTargetUserReader todayMomentTargetUserReader(
            @Value("#{jobParameters['userId']}") String userId,
            @Value("#{jobParameters['date']}") String date) {
        return new TodayMomentTargetUserReader(todayRedisService, userId, date);
    }


    @Bean
    @StepScope
    public TodayDiaryReader todayDiaryReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayDiaryReader(todayDiaryRedisService, date);
    }


    @Bean
    public CompositeItemWriter<TodayDiaryContext> todayDiaryWriter() {
        CompositeItemWriter<TodayDiaryContext> writer = new CompositeItemWriter<>();

        List<ItemWriter<? super TodayDiaryContext>> writers = new ArrayList<>();
        writers.add(dailySummaryWriter);
        writers.add(dailyMomentWriter);

        writer.setDelegates(writers);

        return writer;
    }

}
