package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiaryTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserTagRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserTagRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        executeBatchUpdate(params);
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

    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_USER_TAGS, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO USER_TAGS - {}/{}건", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 USER_TAGS 삽입 실패", e);
            throw e;
        }
    }
}
