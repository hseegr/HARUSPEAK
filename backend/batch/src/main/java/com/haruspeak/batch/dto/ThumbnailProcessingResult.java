package com.haruspeak.batch.dto;

import java.util.List;

public record ThumbnailProcessingResult(
        List<ThumbnailUpdateContext> successList,
        List<ThumbnailUpdateContext> failedList
) {
}
