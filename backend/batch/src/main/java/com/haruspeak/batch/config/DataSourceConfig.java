package com.haruspeak.batch.config;


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배치 작업에 사용되는 RDB
 */
@Configuration
public class DataSourceConfig {

    /////////////////////////// BATCH ////////////////////////////////////////////////

    /**
     * 스프링 배치에서 사용하는 배치 전용 DB
     * - Primary는 배치로 지정
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * batchDataSource - 트랜잭션 매니저
     */
    @Bean
    @Primary
    public PlatformTransactionManager batchTransactionManager(
            @Qualifier("batchDataSource") DataSource batchDataSource) {
        return new JdbcTransactionManager(batchDataSource);
    }


    /////////////////////////// API //////////////////////////////////////////////////

    /**
     * api 서버에서 사용하는 DB (운영 DB)
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.api")
    public DataSource apiDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * apiDataSource - 트랜잭션 매니저
     */
    @Bean
    public PlatformTransactionManager apiTransactionManager(
            @Qualifier("apiDataSource") DataSource businessDataSource) {
        return new JdbcTransactionManager(businessDataSource);
    }

}