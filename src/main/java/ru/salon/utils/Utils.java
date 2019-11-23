package ru.salon.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            .withZone(ZoneId.systemDefault());

    public static String getMonth(int month) {
        switch (month) {
            case 2:
                return "февраля";
            case 3:
                return "марта";
            case 4:
                return "апреля";
            case 5:
                return "мая";
            case 6:
                return "июня";
            case 7:
                return "июля";
            case 8:
                return "августа";
            case 9:
                return "сентября";
            case 10:
                return "октября";
            case 11:
                return "ноября";
            case 12:
                return "декабря";
            default:
                return "января";
        }
    }
}
