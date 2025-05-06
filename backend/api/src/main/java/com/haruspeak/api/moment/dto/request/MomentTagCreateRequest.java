package com.haruspeak.api.moment.dto.request;

import java.util.List;

public record MomentTagCreateRequest(
        List<String> tags,
        String createdAt,
        String content
) {
}
