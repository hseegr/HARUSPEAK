package com.haruspeak.batch.dto.context;

import java.util.List;

public record ThumbnailProcessingResult(
        List<ThumbnailUpdateContext> successList,
        List<ThumbnailGenerateContext> failedList
) {
}
