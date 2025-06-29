package com.github.imrezol.trelloexporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter formatterWithTimeZone =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV").withZone(ZoneId.systemDefault());
    public static String lineSeparator = "*".repeat(20);

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    public static String dateToString(ZonedDateTime dateTime){
        return formatter.format(dateTime);
    }
    public static String dateToStringWithTimeZone(ZonedDateTime dateTime){
        return formatterWithTimeZone.format(dateTime);
    }

    public static void ensureDirectory(String dir) {
        new File(dir).mkdirs();
    }

    public static String getUrl(String filename, String... dirParts) {
        String dirName = String.join("/", dirParts);
        return dirName + "/" + filename;
    }

    public static void saveToFile(Path path, String json) {
        try {
            ensureDirectory(path.getParent().toString());
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pad(int level, String string) {
        return String.format("%s%s", " ".repeat(2*level), string);
    }

}
