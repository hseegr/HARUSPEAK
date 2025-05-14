package com.haruspeak.batch.model.repository;

import com.haruspeak.batch.model.DailyMoment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Repository
public class MomentTagRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MomentTagRepository (@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_INSERT_MOMENT_TAGS =
            """
            INSERT INTO moment_tags (moment_id, user_tag_id)
            SELECT m.moment_id, t.user_tag_id
            FROM active_daily_moments m, user_tag_details t
            WHERE m.user_id = :userId
            AND m.moment_time = :momentTime
            AND t.user_id = :userId
            AND t.name = :name
            ON DUPLICATE KEY UPDATE moment_id = m.moment_id
            """;

    public void bulkInsertMomentTags(List<DailyMoment> dailyMoments) {
        log.debug("🐛 INSERT INTO MOMENT_TAGS");
        dailyMoments.forEach(moment -> log.debug("tags for moment: {}", moment.getTags()));
        SqlParameterSource[] params = buildParams(dailyMoments);
        executeBatchUpdate(params);
    }

    DateTimeFormatter formatterWithNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SqlParameterSource[] buildParams(List<DailyMoment> dailyMoments) {
        return dailyMoments.stream()
                .flatMap(moment -> moment.getTags().stream()
                        .map(tagName -> {
                            MapSqlParameterSource params = new MapSqlParameterSource();
                            params.addValue("userId", moment.getUserId());

                            try {
                                LocalDateTime dateTime = LocalDateTime.parse(fixDateTimePrecision(moment.getMomentTime()).substring(0, 29), formatterWithNano);

                                // 초 단위 반올림 (0.5초 이상이면 반올림)
                                LocalDateTime roundedDateTime = roundToNextSecond(dateTime);

                                // 초 단위까지 표시하기 위한 포맷
                                DateTimeFormatter formatterWithoutNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                                String formattedDate = roundedDateTime.format(formatterWithoutNano);

                                params.addValue("momentTime", formattedDate);
                            } catch (Exception e) {
                                log.error("Failed to parse momentTime: {}", moment.getMomentTime(), e);
                            }

//                            params.addValue("momentTime", moment.getMomentTime());
                            params.addValue("name", tagName);
                            log.debug("moment_tags parmas: {}", params);
                            return params;
                        })
                ).toArray(SqlParameterSource[]::new);
    }

    private void executeBatchUpdate(SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_MOMENT_TAGS, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT INTO MOMENT_TAGS - {}/{}건", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 MOMENT_TAGS 삽입 실패", e);
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

    private static String fixDateTimePrecision(String momentTime) {
        if (momentTime.length() == 19) {
            return momentTime + ".000000000";
        } else if (momentTime.length() == 23) {
            // 마이크로초가 6자리일 경우, 나노초까지 0을 추가
            return momentTime + "000000";
        } else if (momentTime.length() == 26) {
            // 나노초가 9자리면 그대로 리턴
            return momentTime + "000";
        }else if (momentTime.length() == 29) {
            // 나노초가 9자리면 그대로 리턴
            return momentTime;
        }
        return momentTime;
    }
}
