package com.haruspeak.batch.step;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.reader.MomentTagReader;
import com.haruspeak.batch.service.redis.TagRedisService;
import com.haruspeak.batch.writer.MomentTagWriter;
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
public class TodayTagStepConfig {

    @Value("${basic.batch.chunk}")
    private int chunk;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final TagRedisService tagRedisService;
    private final MomentTagWriter momentTagWriter;

    /**
     * 하루 일기 태그
     * @return
     */
    @Bean
    public Step todayTagSaveStep(){
        return new StepBuilder("todayTagSaveStep", jobRepository)
                .<MomentTagContext, MomentTagContext>chunk(chunk, transactionManager)
                .reader(todayTagReader(null))
                .writer(momentTagWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(2)
                .build();
    }

    @Bean
    @StepScope
    public MomentTagReader todayTagReader(@Value("#{jobParameters['date']}") String date) {
        return new MomentTagReader(tagRedisService, date);
    }
}
