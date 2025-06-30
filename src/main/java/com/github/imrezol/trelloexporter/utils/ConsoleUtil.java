package com.github.imrezol.trelloexporter.utils;

public class ConsoleUtil {

    public static String lineSeparator = "*".repeat(20);

    public static String pad(int level, String string) {
        return " ".repeat(2 * level) + string;
    }
}
