package com.haruspeak.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailUpdateContext {
    private int userId;
    private String writeDate;
    private String content;
    private String imageUrl;
}
