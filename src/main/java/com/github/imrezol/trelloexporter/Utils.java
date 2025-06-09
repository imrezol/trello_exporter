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
}
