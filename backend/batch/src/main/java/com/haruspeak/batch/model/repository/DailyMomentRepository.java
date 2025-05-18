package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DailyMomentRepository {

    private final SqlExecutor sqlExecutor;

    public DailyMomentRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_DAILY_MOMENTS =
            """
            INSERT INTO daily_moments
            (summary_id, content, moment_time, image_count, tag_count, created_at)
            SELECT summary_id, :content, :momentTime, :imageCount, :tagCount, :createdAt
            FROM daily_summary
            WHERE user_id = :userId
            AND write_date = date(:createdAt)
            AND NOT EXISTS (
                SELECT 1 FROM active_daily_moments 
                WHERE user_id = :userId 
                AND moment_time = :momentTime
            ) 
            """;

    /**
     * Chunk 내의 moments를 한 번에 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertDailyMoments(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO DAILY_MOMENTS 실행");
        SqlParameterSource[] params = buildParams(dailyMoments);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_DAILY_MOMENTS,params);
    }

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .map(moment -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", moment.getUserId());
                    params.addValue("content", moment.getContent());
                    params.addValue("momentTime", moment.getMomentTime());
                    params.addValue("imageCount", moment.getImages().size());
                    params.addValue("tagCount", moment.getTags().size());
                    params.addValue("createdAt", moment.getCreatedAt());
                    log.debug("daily_moments params {}", params);
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }
}
