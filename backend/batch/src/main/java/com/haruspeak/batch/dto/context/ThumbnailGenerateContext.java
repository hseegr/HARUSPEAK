package com.haruspeak.batch.dto.context;

public record ThumbnailGenerateContext (
        int userId,
        String writeDate,
        String content
){
}
