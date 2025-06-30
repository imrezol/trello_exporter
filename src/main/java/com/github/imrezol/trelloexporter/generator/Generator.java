package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.exporters.CardExporter;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.utils.FileUtil;
import org.apache.logging.log4j.util.Strings;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Generator {

    String filename(String baseName);

    String escape(String text);

    String begin(String title);

    String end();

    String heading(String text, int level);

    String link(String text, String uri);

    String tableRow(String... cells);

    String tableHeader(String... headers);

    String tableFoot();

    String bold(String str);

    String property(String name, String value);

    String exportedDateProperty();

    String property(String name, ZonedDateTime dateTime);

}
