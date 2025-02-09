package com.project.storemanager_api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    // LocalDateTime -> String 변환 (커스텀 포맷 지원)
    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("날짜 또는 패턴이 올바르지 않습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    // 기본 포맷 ("yyyy-MM-dd HH:mm")
    public static String formatLocalDateTimeDefault(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, "yyyy-MM-dd HH:mm");
    }
}