package com.haruspeak.batch.common.util;

public class TextUtil {

    public static boolean isMeaningless(String text) {
        if (text == null || text.trim().isEmpty()) return true;
        String trimmed = text.trim();
        return trimmed.matches("^[~\\-\\.=*_\\/\\s]{3,}$");
    }
}
