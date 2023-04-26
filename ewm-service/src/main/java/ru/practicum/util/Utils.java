package ru.practicum.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public static String localDateTimeToString(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return date.format(formatter);
    }

}
