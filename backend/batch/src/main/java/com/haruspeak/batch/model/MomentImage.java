package com.haruspeak.batch.model;

import java.util.List;

public record MomentImage (
        int userId,
        String momentTime,
        List<String> images
){
}
