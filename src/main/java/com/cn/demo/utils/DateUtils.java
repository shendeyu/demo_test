package com.cn.demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static LocalDateTime getLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = date != null ? LocalDateTime.parse(date, formatter) : null;
        return localDateTime;
    }
}
