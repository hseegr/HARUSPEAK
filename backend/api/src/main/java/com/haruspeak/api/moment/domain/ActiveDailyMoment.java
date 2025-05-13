package com.haruspeak.api.moment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

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
    private int momentId;

    private int summaryId;

    private int userId;

    private String content;

    private LocalDateTime momentTime;

    private int imageCount;

    private int tagCount;

    private int viewCount;
}
