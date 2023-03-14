package com.sgu.apigateway.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUlti {
    public static String getLocalDateTime(String format) {
        String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        return localTime;
    }

    public static String getLocalDateTime() {
        String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return localTime;
    }

    public static String getLocalTimeStamp() {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
        return timestamp;
    }
}
