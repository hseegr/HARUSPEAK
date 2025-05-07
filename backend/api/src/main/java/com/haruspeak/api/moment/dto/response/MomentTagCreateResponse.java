package com.haruspeak.api.moment.dto.response;

import java.util.List;

public record MomentTagCreateResponse(
    List<String> recommendTags
) {
}
