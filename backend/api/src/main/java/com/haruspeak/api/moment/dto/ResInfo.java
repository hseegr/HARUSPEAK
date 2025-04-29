package com.haruspeak.api.moment.dto;

import java.time.LocalDateTime;

public record ResInfo(
        Integer dataCount,
        LocalDateTime nextCursor,
        Boolean hasMore
) {}
