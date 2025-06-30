package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.utils.DateUtil;

import java.util.List;

public  class BoardsGenerator {

    private Generator generator;
    private List<Board> boards;


    public BoardsGenerator(Generator generator, List<Board> boards) {
        this.generator = generator;
        this.boards = boards;
    }

    public final String baseFilename = "Boards";

    public void generate() {

        System.out.println("Generating boards ...");

        Builder sb = new Builder()
                .append(generator.begin(baseFilename));

        sb.append(generateHeader());

        sb.append(generateBoardsTableHead());
        boards.stream()
                .filter(board -> !board.closed)
                .forEach(board -> {
                    sb.append(generateBoardsRow(board));
                });

        sb.append(generateBoardsTableFoot());

        sb.append(generator.end());

        Utils.saveToFile(Utils.getUrl2(Properties.baseDir, getFilename()), sb.toString());
    }

    private String generateHeader() {
        return new Builder()
                .append(generator.exportedDateProperty())
                .append(generator.heading("Boards:", 1))
                .toString();
    }

    private String generateBoardsTableHead() {
        return new Builder()
                .append(generator.tableHeader("Name", "Description", "Last activity"))
                .toString();
    }

    private String generateBoardsTableFoot() {
        return generator.tableFoot();
    }

    private String generateBoardsRow(Board board) {
        String link = generator.link(board.name, Utils.getUrl2(board.id, getFilename()));

        return generator.tableRow(link, board.desc, DateUtil.dateToString(board.dateLastActivity));
    }

    private String getFilename() {
        return baseFilename + generator.extension();
    }

}
