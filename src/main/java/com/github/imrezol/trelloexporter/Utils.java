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

    public static String lineSeparator = "*".repeat(20);

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Deprecated
    public static String getUrl(String filename, String... dirParts) {
        String dirName = String.join("/", dirParts);
        return dirName + "/" + filename;
    }


    public static String pad(int level, String string) {
        return String.format("%s%s", " ".repeat(2*level), string);
    }

}
