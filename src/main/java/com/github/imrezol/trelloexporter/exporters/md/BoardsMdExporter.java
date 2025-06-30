package com.github.imrezol.trelloexporter.exporters.md;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.exporters.BoardsExporter;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BoardsMdExporter extends BoardsExporter {

    public BoardsMdExporter(List<Board> boards) {
        super(boards);
    }

    @Override
    public String getFilename() {
        return baseFilename + ".md";
    }

    @Override
    public String generateHeader() {
        return new StringBuilder()
                .append(MdUtil.exportedDateProperty())
                .append(MdUtil.heding("Boards:", 1))
                .toString();
    }

    @Override
    protected String generateBoardsTableHead() {
        return new StringBuilder()
                .append(MdUtil.tableHeader("Name", "Description", "Last activity"))
                .toString();
    }

    @Override
    public String generateBoardsRow(Board board) {
        Path boardUrl = Path.of(board.id, getFilename());
        String link = MdUtil.link(board.name, boardUrl.toString());

        return MdUtil.tableRow(link, board.desc, Utils.dateToString(board.dateLastActivity));
    }

    @Override
    protected String generateBoardsTableFoot() {
        return "";
    }

    @Override
    public String generateBegin() {
        return "";
    }

    @Override
    public String generateEnd() {
        return "";
    }

}
