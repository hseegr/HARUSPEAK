package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class MomentImageRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MomentImageRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_INSERT_MOMENT_IMAGES =
            """
            INSERT INTO moment_images (moment_id, image_url)
            SELECT moment_id, :image
            FROM active_daily_moments
            WHERE user_id = :userId
            AND moment_time = :momentTime
            """;

    /**
     * images 한 번에 insert
     * - Moment 별로 UNION을 이용하여 select 한 번으로 moment_id와 함께 insert rows를 만들어 bulk insert
     * @param dailyMoments
     */
    public void bulkInsertMomentImages(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO MOMENT_IMAGES 실행");
        SqlParameterSource[] params = buildParams(dailyMoments);
        executeBatchUpdate(params);
    }

    DateTimeFormatter formatterWithNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .flatMap(moment -> moment.getImages().stream()
                        .map(image -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", moment.getUserId());

                            LocalDateTime dateTime = LocalDateTime.parse(moment.getMomentTime(), formatterWithNano);
                            // 초 단위 반올림 (0.5초 이상이면 반올림)
                            LocalDateTime roundedDateTime = roundToNextSecond(dateTime);
                            // 초 단위까지 표시하기 위한 포맷
                            DateTimeFormatter formatterWithoutNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            String formattedDate = roundedDateTime.format(formatterWithoutNano);


                            params.addValue("momentTime", formattedDate);
//                            params.addValue("momentTime", moment.getMomentTime());
                            params.addValue("image", image);
                            log.debug("moment_images parmas: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }

    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_MOMENT_IMAGES, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO MOMENT_IMAGES - {}/{}건", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 MOMENT_IMAGES 삽입 실패", e);
            throw e;
        }
    }

    public static LocalDateTime roundToNextSecond(LocalDateTime dateTime) {
        long nanoAdjustment = dateTime.getNano();

        // 500,000,000 나노초 이상이면 반올림
        if (nanoAdjustment >= 500_000_000) {
            return dateTime.plusSeconds(1).truncatedTo(ChronoUnit.SECONDS); // 반올림
        } else {
            return dateTime.truncatedTo(ChronoUnit.SECONDS); // 잘라내기
        }
    }
}
