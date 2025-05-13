package org.railway.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimerFormat {
    public static String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}