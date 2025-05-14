package com.haruspeak.batch.test;

import com.haruspeak.batch.model.DailySummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TestRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TestRepository (@Qualifier("batchNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void bulkInsertBatchTest(List<String> contents) {
        log.debug("🐛 TEST - INSERT INTO BATCH_TEST FOR CONNECTION TEST");
        String sql =
                """
                INSERT INTO batch_test (content) 
                VALUES (:content)
                """;

        SqlParameterSource[] batchParams = contents.stream()
                .map(content -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("content", content);
                    return params;
                })
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }

}
