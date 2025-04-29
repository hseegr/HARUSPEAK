package com.haruspeak.api.moment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MomentDetailRaw(
        Integer momentId,
        @JsonFormat(timezone = "Asia/Seoul")
        LocalDateTime momentTime,
        String imageUrls,
        String content,
        String tags
) {
}
