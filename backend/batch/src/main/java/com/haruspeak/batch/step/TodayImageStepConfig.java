package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.context.MomentImageContext;
import com.haruspeak.batch.reader.TodayImageReader;
import com.haruspeak.batch.service.redis.ImageRedisService;
import com.haruspeak.batch.writer.MomentImageWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodayImageStepConfig {
    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ImageRedisService imageRedisService;
    private final MomentImageWriter momentImageWriter;



    /**
     * 하루 일기 이미지
     * @return
     */
    @Bean
    public Step todayImageSaveStep(){
        return new StepBuilder("todayImageSaveStep", jobRepository)
                .<MomentImageContext, MomentImageContext>chunk(CHUNK_SIZE, transactionManager)
                .reader(todayImageReader(null))
                .writer(momentImageWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }


    @Bean
    @StepScope
    public TodayImageReader todayImageReader(@Value("#{jobParameters['date']}") String date) {
        return new TodayImageReader(imageRedisService, date);
    }


}
