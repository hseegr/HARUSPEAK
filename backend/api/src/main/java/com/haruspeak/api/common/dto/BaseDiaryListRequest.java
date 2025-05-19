package com.haruspeak.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public abstract class BaseDiaryListRequest {

    @Schema(description = "이전 목록 조회 시 받은 커서값", nullable = true, example = "2025-05-01T15:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime before;

    @Schema(description = "조회 시작일", nullable = true, example = "2025-05-01")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate startDate;

    @Schema(description = "조회 종료일", nullable = true, example = "2025-05-31")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate endDate;

    @Schema(description = "조회 개수 (1~30)", example = "30")
    @NotNull(message = "조회 개수가 널이어서는 안됩니다.")
    @Min(value = 1, message = "조회 가능한 최소 개수는 1입니다.")
    @Max(value = 30, message = "조회 가능한 최대 개수는 30입니다.")
    protected Integer limit;
}
