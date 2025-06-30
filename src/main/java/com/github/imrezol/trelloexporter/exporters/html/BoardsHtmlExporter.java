package com.github.imrezol.trelloexporter.exporters.html;

import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.exporters.BoardsExporter;
import com.github.imrezol.trelloexporter.exporters.md.MdUtil;
import com.github.imrezol.trelloexporter.trello.dto.Board;

import java.nio.file.Path;
import java.util.List;

public class BoardsHtmlExporter extends BoardsExporter {

    public BoardsHtmlExporter(List<Board> boards) {
        super(boards);
    }

    @Override
    public String getFilename() {
        return baseFilename + ".html";
    }

    @Override
    public String generateHeader() {
        return new StringBuilder()
                .append(HtmlUtil.exportedDateProperty())
                .append(HtmlUtil.heding("Boards:", 1))
                .toString();
    }

    @Override
    protected String generateBoardsTableHead() {
        return new StringBuilder()
                .append(HtmlUtil.tableHeader("Name", "Description", "Last activity"))
                .toString();
    }

    @Override
    public String generateBoardsRow(Board board) {
        Path boardUrl = Path.of(board.id, getFilename());
        String link = HtmlUtil.link(board.name, boardUrl.toString());

        return HtmlUtil.tableRow(link, board.desc, Utils.dateToString(board.dateLastActivity));
    }

    @Override
    protected String generateBoardsTableFoot() {
        return HtmlUtil.tableFoot();
    }

    @Override
    public String generateBegin() {
        return HtmlUtil.begin(baseFilename);
    }

    @Override
    public String generateEnd() {
        return HtmlUtil.end();
    }

}
