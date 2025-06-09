package com.github.imrezol.trelloexporter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    public static void ensureDirectory(String dir) {
        new File(dir).mkdirs();
    }

    public static String getFilename(String filename, String... dirParts) {
        String dirName = String.join("/", dirParts);
        ensureDirectory(dirName);
        return dirName + "/" + filename;
    }

    public static void saveToFile(String filename, String json) {
        try {
            Files.writeString(Path.of(filename), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
