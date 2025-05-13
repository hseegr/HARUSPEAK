package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MomentTagRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void bulkInsertMomentTags(List<DailyMoment> dailyMoments) {
        log.debug("🐛 STEP2.WRITE - INSERT MOMENT_TAGS");
        String sql =
                """
                INSERT INTO moment_tags (moment_id, user_tag_id)
                SELECT moment_id, user_tag_id
                FROM active_daily_moments m, user_tag_details t
                WHERE m.user_id = :userId
                AND m.moment_time = :momentTime
                AND t.user_id = :userId
                AND t.name IN (:tags) 
                """;

        List<SqlParameterSource> batchParams = new ArrayList<>();

        for (DailyMoment moment : dailyMoments) {
            MapSqlParameterSource param = new MapSqlParameterSource();
            param.addValue("userId", moment.getUserId());
            param.addValue("momentTime", moment.getMomentTime());
            param.addValue("tags", moment.getTags()); // 직접 List<String>으로 전달

            batchParams.add(param);
        }

        SqlParameterSource[] paramArray = batchParams.toArray(new SqlParameterSource[0]);
        namedParameterJdbcTemplate.batchUpdate(sql, paramArray);
    }

}
