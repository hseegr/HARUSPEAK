package com.haruspeak.batch.common.util;

import java.time.LocalDate;

public class DateFormatUtil {

    public static String formatToKoreanDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return String.format("%d년 %d월 %d일", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
}
