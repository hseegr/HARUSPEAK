package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentDetailRaw(
        int summaryId,
        int momentId,
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        int imageCount,
        List<String> images,
        String content,
        int tagCount,
        List<String> tags
) {
}
