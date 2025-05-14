package com.haruspeak.batch.model.repository;


import com.haruspeak.batch.model.DailyMoment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Repository

public class TagRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TagRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_INSERT_TAGS =
            """
            INSERT INTO tags (name) VALUES (:name)
            ON DUPLICATE KEY UPDATE name = :name
            """;

    /**
     * 사용된 Tag를 모두 추가
     * @param tags
     */
    public void bulkInsertTags(List<String> tags) {
        log.debug("🐛 INSERT INTO TAGS 실행");
        log.debug("Tags: {}", tags);

        SqlParameterSource[] params = buildParams(tags);
        executeBatchUpdate(params);
    }


    private SqlParameterSource[] buildParams(List<String> tags) {
        return  tags.stream()
                .map(tag -> {
                    MapSqlParameterSource params = new MapSqlParameterSource("name", tag);
                    log.debug("tags parmas: {}", params);
                    return params;
                })
                .toArray(SqlParameterSource[]::new);
    }


    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_TAGS, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO TAGS - {}/{}건", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 TAGS 삽입 실패", e);
            throw e;
        }
    }
}
