package com.haruspeak.api.moment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentDetailResponse(
    Integer momentId,
    @JsonFormat(timezone = "Asia/Seoul")
    LocalDateTime momentTime,
    List<String> images,
    String content,
    List<String> tags
) {
}
