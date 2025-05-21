package com.haruspeak.batch.common.util;

public class TextUtil {

    public static boolean isMeaningless(String text) {
        return removeMeaninglessParts(text).isEmpty();
    }

    public static String removeMeaninglessParts(String text) {
        if (text == null || text.trim().isEmpty()) return "";
        String cleaned = text;
        cleaned = cleaned.replaceAll("[~\\-\\.=*_\\/]{3,}", " ");
        cleaned = cleaned.replaceAll("[\\p{So}\\p{Cn}]", " ");
        cleaned = cleaned.replaceAll("([ㅋㅎㅠㅜ])\\1{2,}", " ");
        cleaned = cleaned.replaceAll("[ㄱ-ㅎㅏ-ㅣ]{2,}", " ");
        cleaned = cleaned.replaceAll("\\s{2,}", " ").trim();
        return cleaned;
    }
}
