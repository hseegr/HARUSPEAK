package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailySummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DailySummaryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DailySummaryRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_INSERT_DAILY_SUMMARY =
            """
            INSERT INTO daily_summary 
            (user_id, write_date, title, content, image_url, moment_count) 
            VALUES (:userId, :writeDate, :title, :content, :imageUrl, :momentCount)
            """;

    private static final String SQL_INSERT_DAILY_SUMMARY_WITHOUT_THUMBNAIL =
            """
            INSERT INTO daily_summary 
            (user_id, write_date, title, content, image_url, moment_count) 
            VALUES (:userId, :writeDate, :title, :content, :imageUrl, :momentCount)
            """;

    private static final String SQL_UPDATE_DAILY_SUMMARY_SET_THUMNBAIL =
            """
            UPDATE daily_summary
            SET image_url = :imageUrl
            WHERE user_id = :user_id
            AND write_date = :writeDate
            """;

    /**
     * Chunk의 summary 들을 한 번에 insert
     * @param dailySummaries
     */
    public void bulkInsertDailySummaries(List<DailySummary> dailySummaries) {
        log.debug("🐛 INSERT INTO DAILY_SUMMARY 실행");
        SqlParameterSource[] params = buildParams(dailySummaries);
        executeBatchUpdate(params);
    }

    private SqlParameterSource[] buildParams(List<DailySummary> dailySummaries) {
        return dailySummaries.stream()
                .map(summary ->{
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", summary.getUserId());
                    params.addValue("writeDate", summary.getWriteDate());
                    params.addValue("title", summary.getTitle());
                    params.addValue("content", summary.getContent());
                    params.addValue("imageUrl", summary.getImageUrl());
                    params.addValue("momentCount", summary.getMomentCount());
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }

    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_DAILY_SUMMARY_WITHOUT_THUMBNAIL, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO DAILY_SUMMARY 완료 - {}/{}건", successCount, totalCount);
            }
            
        } catch (Exception e) {
            log.error("💥 DAILY_SUMMARY 삽입 실패", e);
            throw e;
        }
    }
}
