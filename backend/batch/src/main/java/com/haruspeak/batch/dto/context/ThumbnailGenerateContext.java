package com.haruspeak.batch.dto.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailGenerateContext {
        private int userId;
        private String writeDate;
        private String content;
}
