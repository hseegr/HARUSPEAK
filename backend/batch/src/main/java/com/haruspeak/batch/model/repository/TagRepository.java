package com.haruspeak.batch.model.repository;


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
public class TagRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 사용된 Tag를 모두 추가
     * @param tags
     */
    public void bulkInsertTags(List<String> tags) {
        log.debug("🐛 STEP2.WRITE - INSERT TAGS");
        String sql =
                """
                INSERT INTO tags (name) 
                VALUES (:name)
                ON DUPLICATE KEY UPDATE name=:name
                """;

        SqlParameterSource[] batchParams = tags.stream()
                .map(tag -> new MapSqlParameterSource("name", tag))
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
}
