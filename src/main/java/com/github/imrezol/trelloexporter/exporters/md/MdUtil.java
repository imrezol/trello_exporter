package com.github.imrezol.trelloexporter.exporters.md;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import org.apache.logging.log4j.util.Strings;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
https://google.github.io/styleguide/docguide/style.html
https://www.markdownguide.org/basic-syntax/
 */
public class MdUtil {

    public static String link(String text, String uri) {
        //TODO escape chars
        return "[" + text + "](" + uri + ")";
    }

    public static String heding(String text, int level) {
        return new StringBuilder()
                .append(System.lineSeparator())
                .append(String.format("#".repeat(level)))
                .append(text)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .toString();
    }

    public static String tableRow(String ...cells) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                Arrays.stream(cells)
                        .map(cell -> cell.replace("|","\\|"))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    public static String tableHeader(String ...headers) {
        StringBuilder sb = new StringBuilder();

        sb.append(
                Arrays.stream(headers)
                        .map(header -> header.replace("|","\\|"))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.append(System.lineSeparator());

        sb.append(
                Arrays.stream(headers)
                        .map(header -> "-".repeat(header.length()))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public static String property(String name, String value) {
        return new StringBuilder()
                .append(bold(name))
                .append(": ")
                .append(value)
                .append(System.lineSeparator())
                .toString();
    }

    private static String bold(String str) {
        return "**"+str.replace("*","\\*")+"**";
    }

    public static String exportedDateProperty() {
        return property("Export date", Properties.exportDate);
    }

    public static String property(String name, ZonedDateTime dateTime) {
        return property(name, Utils.dateToString(dateTime));
    }
}
