package com.haruspeak.api.summary.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

/**
 * View - daily_summary: is_deleted = 0
 */
@Entity
@Table(name = "active_daily_summary")
@Immutable
@Getter
public class ActiveDailySummary {
    @Id
    private int summaryId;

    private int userId;

    private LocalDate writeDate;

    private String title;

    private String imageUrl;

    private String content;

    private int imageGenerateCount;

    private int contentGenerateCount;

    private int momentCount;

    private int viewCount;
}