package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentDetailRaw(
        Integer momentId,
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        List<String> imageUrls,
        String content,
        List<String> tags
) {
}
