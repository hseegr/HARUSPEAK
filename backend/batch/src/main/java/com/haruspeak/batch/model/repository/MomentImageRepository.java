package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MomentImageRepository {

    private final SqlExecutor sqlExecutor;

    public MomentImageRepository (SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private static final String SQL_INSERT_MOMENT_IMAGES =
            """
            INSERT INTO moment_images (moment_id, image_url)
            SELECT moment_id, :image
            FROM active_daily_moments
            WHERE user_id = :userId
            AND moment_time = :momentTime
            AND NOT EXISTS (SELECT 1 FROM moment_images WHERE image_url = :imageUrl)
            """;

    /**
     * images 한 번에 insert
     * - Moment 별로 UNION을 이용하여 select 한 번으로 moment_id와 함께 insert rows를 만들어 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertMomentImages(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO MOMENT_IMAGES 실행");
        SqlParameterSource[] params = buildParams(dailyMoments);
        sqlExecutor.executeBatchUpdate(SQL_INSERT_MOMENT_IMAGES,params);
    }

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .flatMap(moment -> moment.getImages().stream()
                        .map(image -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", moment.getUserId());
                            params.addValue("momentTime", moment.getMomentTime());
                            params.addValue("image", image);
                            log.debug("moment_images parmas: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }

}
