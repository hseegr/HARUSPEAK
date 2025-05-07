package com.haruspeak.api.summary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_summary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Integer summaryId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "write_date", nullable = false)
    private LocalDate writeDate;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "image_url", length = 100)
    private String imageUrl;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "image_generate_count", nullable = false)
    private int imageGenerateCount;

    @Column(name = "content_generate_count", nullable = false)
    private int contentGenerateCount;

    @Column(name = "moment_count", nullable = false)
    private int momentCount;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public void updateSummary(String title, String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void deleteSummary(){
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }
}
