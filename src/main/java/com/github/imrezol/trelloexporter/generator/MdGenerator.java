package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.utils.DateUtil;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
https://google.github.io/styleguide/docguide/style.html
https://www.markdownguide.org/basic-syntax/
 */
public class MdGenerator implements Generator {

    @Override
    public String filename(String baseName) {
        return baseName + ".md";
    }

    @Override
    public String escape(String text) {
        return text;
    }

    @Override
    public String begin(String title) {
        return null;
    }

    @Override
    public String end() {
        return null;
    }

    @Override
    public String heading(String text, int level) {
        return new Builder()
                .appendNewLine()
                .append("#".repeat(level))
                .append(text)
                .appendNewLine(2)
                .toString();
    }

    @Override
    public  String link(String text, String uri) {
        //TODO escape chars
        return "[" + text + "](" + uri + ")";
    }

    @Deprecated
    @Override
    public  String linkNewTab(String text, String uri) {
        return link(text, uri);
    }

    @Override
    public  String tableRow(String ...cells) {
        Builder sb = new Builder();
        sb.append(
                Arrays.stream(cells)
                        .map(cell -> cell.replace("|","\\|"))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.appendNewLine();
        return sb.toString();
    }

    @Override
    public  String tableHeader(String ...headers) {
        Builder sb = new Builder();

        sb.append(
                Arrays.stream(headers)
                        .map(header -> header.replace("|","\\|"))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.appendNewLine();

        sb.append(
                Arrays.stream(headers)
                        .map(header -> "-".repeat(header.length()))
                        .collect(Collectors.joining(" | ", "| ", " |"))
        );
        sb.appendNewLine();

        return sb.toString();
    }

    @Override
    public String tableFoot() {
        return null;
    }

    @Override
    public String bold(String str) {
        return "**"+str.replace("*","\\*")+"**";
    }

    @Override
    public String property(String name, String value) {
        return new Builder()
                .append(bold(name))
                .append(": ")
                .appendLine(value)
                .toString();
    }

    @Override
    public String exportedDateProperty() {
        return property("Export date", DateUtil.dateToStringWithTimeZone(DateUtil.exportDate));
    }

    @Override
    public String property(String name, ZonedDateTime dateTime) {
        return property(name, DateUtil.dateToString(dateTime));
    }

    @Override
    public String md(String mdStr) {
        return new Builder()
                .appendNewLine()
                .appendLine(mdStr)
                .toString();
    }

    @Override
    public String rule() {
        return new Builder()
                .appendNewLine()
                .appendLine("---")
                .appendNewLine()
                .toString() ;
    }
}
