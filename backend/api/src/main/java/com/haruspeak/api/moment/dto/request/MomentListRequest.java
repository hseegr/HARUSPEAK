package com.haruspeak.api.moment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//public record MomentListRequest(
//        LocalDateTime before,
//        Integer limit,
//        @DateTimeFormat(pattern = "yyyy-MM-dd")
//        LocalDate startDate,
//        @DateTimeFormat(pattern = "yyyy-MM-dd")
//        LocalDate endDate,
//        List<Integer> userTags
//) {
//}
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "순간 일기 목록 조회 요청")
public record MomentListRequest(

        @Schema(description = "이전 목록 조회 시 받은 커서값", nullable = true, example = "2025-05-01T15:00:00")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime before,

        @Schema(description = "조회 시작일", nullable = true, example = "2025-05-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(description = "조회 종료일", nullable = true, example = "2025-05-31")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @Schema(description = "조회 개수 (1~30)", nullable = true,  example = "30")
        @Min(1)
        @Max(30)
        Integer limit,

        @Schema(description = "사용자 태그 ID 목록", nullable = true,  example = "1,2,3")
        List<Integer> userTags

) {}
