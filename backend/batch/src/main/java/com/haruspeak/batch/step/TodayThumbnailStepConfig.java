package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.reader.ThumbnailReader;
import com.haruspeak.batch.service.redis.ThumbnailRedisService;
import com.haruspeak.batch.writer.ThumbnailUpdateWriter;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodayThumbnailStepConfig {


    @Value("${ai.batch.chunk}")
    private int chunk;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ThumbnailRedisService thumbnailRedisService;
    private final ThumbnailUpdateWriter thumbnailUpdateWriter;


    /**
     * 하루 썸네일 업데이트
     * @return
     */
    @Bean
    public Step todayThumbnailUpdateStep(){
        return new StepBuilder("todayThumbnailUpdateStep", jobRepository)
                .<ThumbnailGenerateContext, ThumbnailGenerateContext>chunk(chunk, transactionManager)
                .reader(thumbnailReader(null))
                .writer(thumbnailUpdateWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }


    @Bean
    @StepScope
    public ThumbnailReader thumbnailReader(@Value("#{jobParameters['date']}") String date) {
        return new ThumbnailReader(thumbnailRedisService, date);
    }

}
