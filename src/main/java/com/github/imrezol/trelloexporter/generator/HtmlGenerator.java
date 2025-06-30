package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.utils.DateUtil;
import org.springframework.web.util.HtmlUtils;

import java.time.ZonedDateTime;


/*
https://github.com/hail2u/html-best-practices
 */
public class HtmlGenerator implements Generator {

    private final String style = """
            <style>
            
            table, th, td {
              border: 1px solid black;
              border-collapse: collapse;
            }
            
            </style>
            """;

    @Override
    public String filename(String baseName) {
        return baseName+ ".html";
    }

    @Override
    public String escape(String text) {
        return HtmlUtils.htmlEscape(text);
    }

    @Override
    public String begin(String title) {
        return new Builder()
                .appendLine("<!DOCTYPE html>")
                .appendLine("<html>")
                .appendLine("<head>")
                .appendLine("<meta charset=\"UTF-8\"> ")
                .appendLine(style)
                .appendLine("<title>").append(escape(title)).append("</title>")
                .appendLine("</head>")
                .appendLine("<body>")
                .toString();
    }

    @Override
    public String end() {
        return new Builder()
                .appendLine("</body>")
                .appendLine("</html>")
                .toString();
    }

    @Override
    public String heading(String text, int level) {
        String tag = "h" + level;
        return new Builder()
                .append("<" + tag + ">")
                .append(escape(text))
                .appendLine("</" + tag + ">")
                .toString();
    }

    @Override
    public String link(String text, String uri) {
        return new Builder()
                .append("<a href=\"").append(uri).append("\">")
                .append(escape(text))
                .append("</a>")
                .toString();
    }

    @Override
    public String tableRow(String... cells) {
        Builder sb = new Builder();
        sb.appendLine("<tr>");
        for (String cell : cells) {
            sb.appendLine("<td>").append(cell).append("</td>");
        }
        sb.appendLine("</tr>");
        return sb.toString();
    }

    @Override
    public String tableHeader(String... headers) {
        Builder sb = new Builder();
        sb.appendLine("<table>");
        sb.appendLine("<tr>");
        for (String header : headers) {
            sb.appendLine("<th>");
            sb.appendLine(header);
            sb.appendLine("</th>");
        }
        sb.appendLine("</tr>");
        return sb.toString();
    }

    @Override
    public String tableFoot() {
        return new Builder()
                .appendLine("</table>")
                .toString();
    }

    @Override
    public String bold(String str) {
        return "<b>" + escape(str) + "</b>";
    }

    @Override
    public String property(String name, String value) {
        return new Builder()
                .append("<div>")
                .append(bold(name))
                .append(": ")
                .append(escape(value))
                .appendLine("/<div>")
                .toString();
    }

    @Override
    public String exportedDateProperty() {
        return property("Export date", DateUtil.dateToStringWithTimeZone(Properties.exportDate));
    }

    @Override
    public String property(String name, ZonedDateTime dateTime) {
        return property(name, DateUtil.dateToString(dateTime));
    }

}
