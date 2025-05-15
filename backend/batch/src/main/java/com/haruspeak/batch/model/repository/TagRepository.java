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

    private final SqlExecutor sqlExecutor;

    public TagRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_TAGS =
            """
            INSERT IGNORE INTO tags (name) VALUES (:name)
            """;

    /**
     * 사용된 Tag를 모두 추가
     * @param tags
     */
    public void bulkInsertTags(List<String> tags) {
        log.debug("🐛 INSERT INTO TAGS 실행");
        SqlParameterSource[] params = buildParams(tags);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_TAGS, params);
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
}
