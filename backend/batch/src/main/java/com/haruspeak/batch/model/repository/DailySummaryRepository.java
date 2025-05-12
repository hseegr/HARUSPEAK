package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class DailySummaryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void bulkInsertDailySummaries(List<DailySummary> dailySummaries) {
        String sql = """
                INSERT INTO daily_summary 
                (user_id, write_date, title, content, image_url, moment_count) 
                VALUES (:userId, :writeDate, :title, :content, :imageUrl, :momentCount)
                """;

        SqlParameterSource[] batchParams = dailySummaries.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
}
