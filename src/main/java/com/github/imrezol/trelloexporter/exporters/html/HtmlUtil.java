package com.github.imrezol.trelloexporter.exporters.html;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
https://github.com/hail2u/html-best-practices
 */
public class HtmlUtil {

    private static final String style = """
            <style>
          
            table, th, td {
              border: 1px solid black;
              border-collapse: collapse;
            }
            
            </style>
            """;

    public static String begin(String title) {
        return new StringBuilder()
                .append("<!DOCTYPE html>").append(System.lineSeparator())
                .append("<html>").append(System.lineSeparator())
                .append("<head>").append(System.lineSeparator())
                .append("<meta charset=\"UTF-8\"> ").append(System.lineSeparator())
                .append(style).append(System.lineSeparator())
                .append("<title>").append(escape(title)).append("</title>").append(System.lineSeparator())
                .append("</head>").append(System.lineSeparator())
                .append("<body>").append(System.lineSeparator())
                .toString();
    }

    private static String escape(String text) {
        // <.>, etc
        return text;
    }

    public static String end() {
        return new StringBuilder()
                .append("</body>").append(System.lineSeparator())
                .append("</html>").append(System.lineSeparator())
                .toString();
    }

    public static String heding(String text, int level) {
        String tag = "h" + level;
        return new StringBuilder()
                .append("<" + tag + ">")
                .append(escape(text))
                .append("</" + tag + ">")
                .append(System.lineSeparator())
                .toString();
    }

    public static String link(String text, String uri) {
        return new StringBuilder()
                .append("<a href=\"").append(uri).append("\">")
                .append(escape(text))
                .append("</a>")
                .toString();
    }

    public static String tableRow(String... cells) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>").append(System.lineSeparator());
        for (String cell : cells) {
            sb.append("<td>").append(escape(cell)).append("</td>").append(System.lineSeparator());
        }
        sb.append("</tr>").append(System.lineSeparator());
        return sb.toString();
    }

    public static String tableHeader(String... headers) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>").append(System.lineSeparator());
        sb.append("<tr>").append(System.lineSeparator());
        for (String header : headers) {
            sb.append("<th>").append(escape(header)).append("</th>").append(System.lineSeparator());
        }
        sb.append("</tr>").append(System.lineSeparator());
        return sb.toString();
    }

    public static String tableFoot() {
        return new StringBuilder()
                .append("</table>").append(System.lineSeparator())
                .toString();
    }

    public static String exportedDateProperty() {
        return ""; //TODO
    }
}
