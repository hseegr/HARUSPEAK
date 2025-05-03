package com.haruspeak.api.moment.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record MomentUpdateRequest(
        String momentTime,
        List<String> images,
        List<String> deletedImages,
        String content,
        List<String> tags
) {
}
