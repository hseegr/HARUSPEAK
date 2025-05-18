package com.haruspeak.batch.dto.context;

import java.util.List;

public record DiaryStepContext (
        List<MomentTagContext> tagContexts,
        List<MomentImageContext> imageContexts,
        List<ThumbnailGenerateContext> thumbnailContexts
){
}
