package com.haruspeak.batch.dto.context;

public record ThumbnailUpdateContext (
        int userId,
        String writeDate,
        String imageUrl
) {
}