package com.haruspeak.api.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class QuerydslSqlConfig {

    private final DataSource dataSource;

    @Bean
    public com.querydsl.sql.Configuration querydslSqlConfiguration() {
        return new com.querydsl.sql.Configuration(MySQLTemplates.builder().build());
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(com.querydsl.sql.Configuration configuration) {
        return new SQLQueryFactory(configuration, () -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
