package com.github.imrezol.trelloexporter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class FileUtil {

    public static final String baseDir = "exports/" +
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(DateUtil.exportDate);

    public static void ensureDirectory(String dir) {
        new File(dir).mkdirs();
    }

    public static String getUrl(String... parts) {
        return String.join(File.separator, parts);

    }

    public static void saveToFile(String filename, String str) {
        Path path = Path.of(filename);
        try {
            ensureDirectory(path.getParent().toString());
            Files.writeString(path, str, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
