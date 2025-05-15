package com.haruspeak.batch.model.repository;

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
            INSERT IGNORE INTO moment_tags (moment_id, user_tag_id)
            SELECT m.moment_id, t.user_tag_id
            FROM active_daily_moments m, user_tag_details t
            WHERE m.user_id = :userId
            AND m.moment_time = :momentTime
            AND t.user_id = :userId
            AND t.name = :name
            """;

    public void bulkInsertMomentTags(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO MOMENT_TAGS");
        SqlParameterSource[] params = buildParams(dailyMoments);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_MOMENT_TAGS, params);
    }

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .flatMap(moment -> moment.getTags().stream()
                        .map(tagName -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", moment.getUserId());
                            params.addValue("momentTime", moment.getMomentTime());
                            params.addValue("name", tagName);
                            log.debug("moment_tags parmas: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }
}
