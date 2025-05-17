package com.haruspeak.batch.dto.context.result;

import com.haruspeak.batch.dto.context.ThumbnailGenerateContext;
import com.haruspeak.batch.dto.context.ThumbnailUpdateContext;

import java.util.List;

public record ThumbnailProcessingResult(
        List<ThumbnailUpdateContext> successList,
        List<ThumbnailGenerateContext> failedList
) {
}
