package com.github.imrezol.trelloexporter.generator;

import java.time.ZonedDateTime;

public interface Generator {

    String filename(String baseName);

    String escape(String text);

    String begin(String title);

    String end();

    String heading(String text, int level);

    String link(String text, String uri);
    String linkNewTab(String text, String uri);

    String tableRow(String... cells);

    String tableHeader(String... headers);

    String tableFoot();

    String bold(String str);

    String property(String name, String value);

    String exportedDateProperty();

    String property(String name, ZonedDateTime dateTime);

    String md(String mdStr);

    String rule();
}
