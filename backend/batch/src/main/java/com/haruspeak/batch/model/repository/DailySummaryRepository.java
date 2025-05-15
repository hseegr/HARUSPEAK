package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.dto.ThumbnailUpdateDTO;
import com.haruspeak.batch.model.DailySummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DailySummaryRepository {

    private final SqlExecutor sqlExecutor;

    public DailySummaryRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_DAILY_SUMMARY =
            """
            INSERT IGNORE INTO daily_summary 
            (user_id, write_date, title, content, image_url, moment_count) 
            VALUES (:userId, :writeDate, :title, :content, :imageUrl, :momentCount)
            """;

    private static final String SQL_INSERT_DAILY_SUMMARY_EXCLUDING_THUMBNAIL =
            """
            INSERT INTO daily_summary 
            (user_id, write_date, title, content, moment_count) 
            VALUES (:userId, :writeDate, :title, :content, :momentCount)
            """;

    private static final String SQL_UPDATE_DAILY_SUMMARY_SET_THUMBNAIL =
            """
            UPDATE daily_summary
            SET image_url = :imageUrl
            WHERE user_id = :userId
            AND write_date = :writeDate
            """;

    /**
     * Chunk의 summary 들을 한 번에 insert
     * @param dailySummaries
     */
    public void bulkInsertDailySummariesWithoutThumbnail(List<DailySummary> dailySummaries) {
        log.debug("🐛 INSERT INTO DAILY_SUMMARY 실행");
        SqlParameterSource[] params = buildInsertParams(dailySummaries);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_DAILY_SUMMARY_EXCLUDING_THUMBNAIL, params);
    }

    /**
     * summary의 썸네일 업데이트
     * @param thumbnails
     */
    public void bulkUpdateThumbnailForDailySummaries(List<ThumbnailUpdateDTO> thumbnails) {
        log.debug("🐛 UPDATE DAILY_SUMMARY SET THUMBNAIL 실행");
        SqlParameterSource[] params = buildUpdateParamsThumbnail(thumbnails);
        sqlExecutor.executeBatchUpdate(SQL_UPDATE_DAILY_SUMMARY_SET_THUMBNAIL, params);
    }

    private SqlParameterSource[] buildInsertParams(List<DailySummary> dailySummaries) {
        return dailySummaries.stream()
                .map(summary ->{
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", summary.getUserId());
                    params.addValue("writeDate", summary.getWriteDate());
                    params.addValue("title", summary.getTitle());
                    params.addValue("content", summary.getContent());
                    params.addValue("momentCount", summary.getMomentCount());
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource[] buildUpdateParamsThumbnail(List<ThumbnailUpdateDTO> thumbnails) {
        return thumbnails.stream()
                .map(thumbnail ->{
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", thumbnail.userId());
                    params.addValue("writeDate", thumbnail.writeDate());
                    params.addValue("imageUrl", thumbnail.imageUrl());
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }
}
