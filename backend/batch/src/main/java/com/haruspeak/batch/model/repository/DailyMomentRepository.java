package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DailyMomentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Chunk 내의 moments를 한 번에 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertDailyMoments(List<DailyMoment> dailyMoments) {
        log.debug("🐛 STEP1.WRITE - INSERT DAILY_MOMENTS");
        String sql =
                """
                INSERT INTO daily_moments
                (summary_id, content, moment_time, image_count, tag_count, created_at)
                SELECT summary_id, :content, :momentTime, :imageCount, :tagCount, :createdAt
                FROM daily_summary
                WHERE user_id = :userId
                AND write_date = date(:createdAt)
                """;

        SqlParameterSource[] batchParams = dailyMoments.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
}
