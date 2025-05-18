package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.dto.context.UserTagInsert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserTagRepository {

    private final SqlExecutor sqlExecutor;

    public UserTagRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_USER_TAGS =
            """
            INSERT INTO user_tags (user_id, tag_id, last_used_at, total_usage_count, score)
            SELECT :userId, tag_id, :lastUsedAt, :usageCount, :usageCount
            FROM tags
            WHERE name = :name
            ON DUPLICATE KEY UPDATE
                last_used_at = IF(last_used_at != :lastUsedAt, :lastUsedAt, last_used_at),
                total_usage_count = IF(last_used_at != :lastUsedAt, total_usage_count + :usageCount, total_usage_count),
                score = IF(last_used_at != :lastUsedAt, score + :usageCount, score)
            """;

    /**
     * 사용자별 태그 추가 또는 업데이트
     * @param inserts
     */
    public void bulkInsertUserTags(List<UserTagInsert> inserts) {
        log.debug("🐛 INSERT INTO USER_TAGS 실행");
        SqlParameterSource[] params = buildParams(inserts);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_USER_TAGS, params);
    }

    private SqlParameterSource[] buildParams(List<UserTagInsert> inserts) {
        return inserts.stream()
                .map(insert -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", insert.userId());
                    params.addValue("lastUsedAt", insert.lastUsedAt());
                    params.addValue("name", insert.tagName());
                    params.addValue("usageCount", insert.usageCount());
                    log.debug("user_tags params: {}", params);
                    return params;
                }).toArray(SqlParameterSource[]::new);
    }
}
