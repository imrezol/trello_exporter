package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;

import java.util.List;

public abstract class BoardsExporter {

    protected List<Board> boards;

    public BoardsExporter(List<Board> boards) {
        this.boards = boards;
    }

    protected BoardsExporter() {
    }

    public abstract String getFilename();

    public abstract String generateBegin();

    public abstract String generateEnd();

    public abstract String generateHeader();

    protected abstract String generateBoardsTableHead();

    public abstract String generateBoardsRow(Board board);

    protected abstract String generateBoardsTableFoot();


    public final String baseFilename = "Boards";

    public void generate() {

        System.out.println("Generating boards ...");

        StringBuilder sb = new StringBuilder()
                .append(generateBegin());


        sb.append(generateHeader());

        sb.append(generateBoardsTableHead());
        boards.stream()
                .filter(board -> !board.closed)
                .forEach(board -> {
                    sb.append(generateBoardsRow(board));
                });

        sb.append(generateBoardsTableFoot());

        sb.append(generateEnd());

        Utils.saveToFile(Utils.getUrl2(Properties.baseDir, getFilename()), sb.toString());
    }

}
