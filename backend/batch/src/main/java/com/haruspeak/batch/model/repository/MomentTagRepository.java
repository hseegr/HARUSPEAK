package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.model.DailyMoment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MomentTagRepository {

    private final SqlExecutor sqlExecutor;

    public MomentTagRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_MOMENT_TAGS =
            """
            INSERT INTO moment_tags (moment_id, user_tag_id)
            SELECT m.moment_id, t.user_tag_id
            FROM active_daily_moments m, user_tag_details t
            WHERE m.user_id = :userId
            AND m.created_at = :createdAt
            AND t.user_id = :userId
            AND t.name = :name
            AND NOT EXISTS (
                SELECT 1 
                FROM moment_tag_names mt
                JOIN daily_moments dm
                ON mt.moment_id = dm.moment_id
                WHERE mt.user_id = :userId 
                AND dm.created_at = :createdAt
                AND mt.name = :name
            )
            """;


    public void bulkInsertMomentTags(List<MomentTagContext> contexts) {
        log.debug("🐛 INSERT INTO MOMENT_TAGS");
        SqlParameterSource[] params = buildParams(contexts);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_MOMENT_TAGS, params);
    }

    private SqlParameterSource[] buildParams(List<MomentTagContext> contexts) {
        return contexts.stream()
                .flatMap(moment -> moment.getTags().stream()
                        .map(tagName -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", moment.getUserId());
                            params.addValue("createdAt", moment.getCreatedAt());
                            params.addValue("name", tagName);
                            log.debug("moment_tags params: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }
}
