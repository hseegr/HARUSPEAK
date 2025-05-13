package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.TodayDiaryTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserTagRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 사용자별 태그 추가 또는 업데이트
     * @param diaryTags
     * @param date
     */
    public void bulkInsertUserTags(List<TodayDiaryTag> diaryTags, String date) {
        log.debug("🐛 STEP2.WRITE - INSERT USER_TAGS");
        String sql =
                """
                INSERT INTO user_tags (user_id, tag_id, last_used_at,total_usage_count, score)
                SELECT :userId, tag_id, :date, :useCount, :useCount
                FROM tags
                WHERE name = :name
                ON DUPLICATE KEY UPDATE 
                     last_used_at = :date,
                     total_usage_count = total_usage_count + :useCount,
                     score = score + :userCount
                """;

        SqlParameterSource[] batchParams = diaryTags.stream()
                .map(tagInfo -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("userId", tagInfo.getUserId());
                    params.addValue("date", date);

                    for (Map.Entry<String, Integer> entry : tagInfo.getTagCountMap().entrySet()) {
                        params.addValue("name", entry.getKey());
                        params.addValue("useCount", entry.getValue());
                    }
                    return params;
                })
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
}
