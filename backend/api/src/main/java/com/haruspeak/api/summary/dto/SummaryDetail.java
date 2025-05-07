package com.haruspeak.api.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "순간 일기 상세 정보")
public record SummaryDetail(
        @Schema(description = "하루일기ID", example = "1")
        int summaryId,
        @Schema(description = "작성일자", example = "2025-05-01")
        LocalDate diaryDate,
        @Schema(description = "요약 이미지 주소", example = "https://s3.example.com/image.png")
        String imageUrl,
        @Schema(description = "일기 제목", example = "일기 제목입니다.")
        String title,
        @Schema(description = "요약 내용", example = "요약 내용입니다.")
        String content,
        @Schema(description = "이미지 생성중 상태", example = "false")
        boolean isImageGenerating,
        @Schema(description = "AI 요약 이미지 생성 횟수", example = "1")
        int imageGenerateCount,
        @Schema(description = "AI 요약 내용 생성 횟수", example = "1")
        int contentGenerateCount,
        @Schema(description = "순간 일기 개수 ", example = "5")
        int momentCount
){
}
