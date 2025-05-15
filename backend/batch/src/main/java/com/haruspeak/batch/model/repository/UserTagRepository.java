package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.TodayDiaryTag;
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
            INSERT INTO user_tags (user_id, tag_id, last_used_at,total_usage_count, score)
            SELECT :userId, tag_id, :date, :useCount, :useCount
            FROM tags
            WHERE name = :name
            ON DUPLICATE KEY UPDATE 
                 last_used_at = :date,
                 total_usage_count = total_usage_count + :useCount,
                 score = score + :useCount
            """;

    /**
     * 사용자별 태그 추가 또는 업데이트
     * @param diaryTags
     * @param date
     */
    public void bulkInsertUserTags(List<TodayDiaryTag> diaryTags, String date) {
        log.debug("🐛 INSERT INTO USER_TAGS 실행");
        SqlParameterSource[] params = buildParams(diaryTags, date);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_USER_TAGS, params);
    }

    private SqlParameterSource[] buildParams(List<TodayDiaryTag> diaryTags, String date) {
        return diaryTags.stream()
                .flatMap(tagInfo -> tagInfo.getTagCountMap().entrySet().stream()
                        .map(entry -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", tagInfo.getUserId());
                            params.addValue("date", date);
                            params.addValue("name", entry.getKey());
                            params.addValue("useCount", entry.getValue());
                            log.debug("user_tags parmas: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }
}
