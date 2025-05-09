package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentListItemRaw(
        int summaryId,
        int momentId,
        int orderInDay,
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        int imageCount,
        List<String> images,
        String content,
        int tagCount,
        List<String> tags
) {
}
