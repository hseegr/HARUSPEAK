package com.haruspeak.batch.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqlExecutor {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SqlExecutor(@Qualifier("apiNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void executeBatchUpdate(String sql, SqlParameterSource[] params) {
        try {
            int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, params);

            if (log.isDebugEnabled()) {
                int successCount = 0;
                int totalCount = updateCounts.length;

                for (int count : updateCounts) {
                    if (count > 0) {
                        successCount++;
                    }
                }

                log.debug("🐛 INSERT - {}/{}건 완료", successCount, totalCount);
            }
        } catch (Exception e) {
            log.error("💥 INSERT 실패", e);
            throw e;
        }
    }
}