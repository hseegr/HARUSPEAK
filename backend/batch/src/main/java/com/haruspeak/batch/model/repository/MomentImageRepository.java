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
public class MomentImageRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * images 한 번에 insert
     * - Moment 별로 UNION을 이용하여 select 한 번으로 moment_id와 함께 insert rows를 만들어 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertMomentImages(List<DailyMoment> dailyMoments) {
        log.debug("🐛 STEP3.WRITE - INSERT MOMENT_IMAGES");
        String sql =
            """
            INSERT INTO moment_images (moment_id, image_url)
            SELECT moment_id, image
            FROM active_daily_moments
            CROSS JOIN ( :images ) AS images
            WHERE user_id = :userId
            AND moment_time = :momentTime;
            """;

        List<SqlParameterSource> batchParams = new ArrayList<>();

        for (DailyMoment moment : dailyMoments) {
            StringBuilder imagesUnionQuery = new StringBuilder();
            List<String> images = moment.getImages();

            for (String image : images) {
                if (!imagesUnionQuery.isEmpty()) {
                    imagesUnionQuery.append(" UNION ");
                }
                imagesUnionQuery.append("SELECT '").append(image).append("' AS image");
            }

            MapSqlParameterSource param = new MapSqlParameterSource();
            param.addValue("images", imagesUnionQuery.toString());
            param.addValue("userId", moment.getUserId());
            param.addValue("momentTime", moment.getMomentTime());

            batchParams.add(param);
        }

        SqlParameterSource[] paramArray = batchParams.toArray(new SqlParameterSource[0]);
        namedParameterJdbcTemplate.batchUpdate(sql, paramArray);
    }
}
