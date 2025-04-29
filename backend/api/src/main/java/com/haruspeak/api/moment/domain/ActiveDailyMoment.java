package com.haruspeak.api.moment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

/**
 * View - daily_moments
 * - is_deleted = 0
 * - 다른 정보 포함
 */

@Entity
@Table(name = "active_daily_moments")
@Immutable
@Getter
public class ActiveDailyMoment {
    @Id
    private Integer momentId;

    private Integer summaryId;

    private String content;

    private java.sql.Timestamp momentTime;

    private Integer imageCount;

    private Integer tagCount;

    private Integer viewCount;

    private String imageUrls;

    private String tags;
}
