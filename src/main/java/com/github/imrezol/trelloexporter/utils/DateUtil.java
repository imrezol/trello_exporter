package com.github.imrezol.trelloexporter.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter dueFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter formatterWithTimeZone =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV").withZone(ZoneId.systemDefault());

    public static String dueToString(ZonedDateTime dateTime){
        return dueFormatter.format(dateTime);
    }

    public static String dateToString(ZonedDateTime dateTime){
        return formatter.format(dateTime);
    }

    public static String dateToStringWithTimeZone(ZonedDateTime dateTime){
        return formatterWithTimeZone.format(dateTime);
    }
}
