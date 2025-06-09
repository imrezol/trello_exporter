package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
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

@Service
public class BoardsExporter {

    private static final Logger logger = LoggerFactory.getLogger(BoardsExporter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    @Autowired
    private BoardExporter boardExporter;

    public void export(){

        logger.info("Exporting boards ...");

        List<Board> boards = trelloApi.getBoards()
                .stream()
                .filter(board -> !properties.skipArchives || !board.closed)
                .toList();

        StringBuilder sb = new StringBuilder()
                .append(new Heading("Boards:", 1)).append("\n");

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT)
                .addRow("Name", "Description");

        Utils.ensureDirectory(properties.baseDir);
        Path fileName = Paths.get(properties.baseDir, properties.getBoardsMd());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {
            writer.write(sb.toString());
            writer.newLine();

            boards.forEach(board -> {
                String boardUrl = Utils.getUrl(properties.getBoardMd() , board.id);
                Link link = new Link(board.name, boardUrl);

                tableBuilder.addRow(link, board.desc);

                boardExporter.export(board);
            });

            writer.write(tableBuilder.build().toString());
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
