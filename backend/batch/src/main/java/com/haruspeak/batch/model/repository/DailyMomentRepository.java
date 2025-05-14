package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.DailySummary;
import lombok.RequiredArgsConstructor;
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
public class DailyMomentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DailyMomentRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_INSERT_DAILY_MOMENTS =
            """
            INSERT INTO daily_moments
            (summary_id, content, moment_time, image_count, tag_count, created_at)
            SELECT summary_id, :content, :momentTime, :imageCount, :tagCount, :createdAt
            FROM daily_summary
            WHERE user_id = :userId
            AND write_date = date(:createdAt)
            """;

    /**
     * Chunk 내의 moments를 한 번에 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertDailyMoments(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO DAILY_MOMENTS 실행");
        SqlParameterSource[] params = buildParams(dailyMoments);
        executeBatchUpdate(params);
    }

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .map(moment -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", moment.getUserId());
                    params.addValue("content", moment.getContent());
                    params.addValue("momentTime", moment.getMomentTime());
                    params.addValue("imageCount", moment.getImageCount());
                    params.addValue("tagCount", moment.getTagCount());
                    params.addValue("createdAt", moment.getCreatedAt());
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }

    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_DAILY_MOMENTS, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO DAILY_MOMENTS 완료 - {}/{}건", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 DAILY_MOMENTS 삽입 실패", e);
            throw e;
        }
    }
}
