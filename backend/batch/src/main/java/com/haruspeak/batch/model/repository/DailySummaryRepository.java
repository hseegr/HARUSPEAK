package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DailySummaryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Chunk의 summary 들을 한 번에 insert
     * @param dailySummaries
     */
    public void bulkInsertDailySummaries(List<DailySummary> dailySummaries) {
        log.debug("🐛 STEP1.WRITE - INSERT DAILY_SUMMARY");
        String sql =
                """
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
