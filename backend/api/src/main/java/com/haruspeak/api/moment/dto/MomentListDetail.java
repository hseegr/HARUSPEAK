package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentListDetail(
        Integer summaryId,
        Integer momentId,
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        Integer imageCount,
        List<String> images,
        String content,
        Integer tagCount,
        List<String> tags
) {
}
