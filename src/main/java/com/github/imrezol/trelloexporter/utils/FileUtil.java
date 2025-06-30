package com.github.imrezol.trelloexporter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static void ensureDirectory(String dir) {
        new File(dir).mkdirs();
    }

    public static String getUrl2(String... parts) {
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
