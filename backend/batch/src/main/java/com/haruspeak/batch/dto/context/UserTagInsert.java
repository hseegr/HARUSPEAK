package com.haruspeak.batch.dto.context;

public record UserTagInsert (
   int userId,
   String lastUsedAt,
   String tagName,
   int usageCount
) {
}
