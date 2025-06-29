package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BoardsExporter {

    private static final Logger logger = LoggerFactory.getLogger(BoardsExporter.class);

    public static void export(List<Board> boards) {

        System.out.println("Exporting boards ...");

        StringBuilder sb = new StringBuilder()
                .append("Export date: ").append(Utils.dateToStringWithTimeZone(Properties.exportDate)).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Heading("Boards:", 1)).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator());


        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT)
                .addRow("Name", "Description", "Last activity");

        Utils.ensureDirectory(Properties.baseDir);
        Path fileName = Paths.get(Properties.baseDir, Properties.getBoardsMd());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {
            writer.write(sb.toString());
            writer.newLine();

            boards.stream()
                    .filter(board -> !board.closed)
                    .forEach(board -> {
                        String boardUrl = Utils.getUrl(Properties.getBoardMd(), board.id);
                        Link link = new Link(board.name, boardUrl);

                        tableBuilder.addRow(link, board.desc, Utils.dateToString(board.dateLastActivity));

                    });

            writer.write(tableBuilder.build().toString());
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
