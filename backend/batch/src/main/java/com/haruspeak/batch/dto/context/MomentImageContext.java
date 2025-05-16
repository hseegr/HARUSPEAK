package com.haruspeak.batch.dto.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomentImageContext {
    private int userId;
    private String momentTime;
    private Set<String> images;
}

