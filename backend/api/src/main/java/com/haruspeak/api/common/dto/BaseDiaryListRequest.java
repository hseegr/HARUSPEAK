package com.haruspeak.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public abstract class BaseDiaryListRequest {

    @Schema(description = "이전 목록 조회 시 받은 커서값", nullable = true, example = "2025-05-01T15:00:00")
    String before;

    @Schema(description = "조회 시작일", nullable = true, example = "2025-05-01")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}",
            message = "잘못된 날짜 형식입니다. (예: 2025-05-19)"
    )
    protected String startDate;

    @Schema(description = "조회 종료일", nullable = true, example = "2025-05-31")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}",
            message = "잘못된 날짜 형식입니다. (예: 2025-05-19)"
    )
    protected String endDate;

    @Schema(description = "조회 개수 (1~30)", example = "30")
    @NotNull(message = "조회 개수가 널이어서는 안됩니다.")
    @Min(value = 1, message = "조회 가능한 최소 개수는 1입니다.")
    @Max(value = 30, message = "조회 가능한 최대 개수는 30입니다.")
    protected Integer limit;
}
