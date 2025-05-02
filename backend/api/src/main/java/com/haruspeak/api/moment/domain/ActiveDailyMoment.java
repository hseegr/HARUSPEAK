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
    private Integer momentId;

    private Integer summaryId;

    private Integer userId;

    private String content;

    private LocalDateTime momentTime;

    private Integer imageCount;

    private Integer tagCount;

    private Integer viewCount;

//    private String imageUrls;
//
//    private String tags;
}
