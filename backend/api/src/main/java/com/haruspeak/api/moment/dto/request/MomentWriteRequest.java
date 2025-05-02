package com.haruspeak.api.moment.dto.request;

import java.util.List;

public record MomentWriteRequest(
        String content,
        List<String> images
) {
}
