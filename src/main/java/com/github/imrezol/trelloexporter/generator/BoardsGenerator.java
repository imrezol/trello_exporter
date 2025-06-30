package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.utils.DateUtil;
import com.github.imrezol.trelloexporter.utils.FileUtil;

import java.util.List;

public class BoardsGenerator {

    private Generator generator;
    private List<Board> boards;


    public BoardsGenerator(Generator generator, List<Board> boards) {
        this.generator = generator;
        this.boards = boards;
    }

    public final static String baseFilename = "Boards";

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

        FileUtil.saveToFile(FileUtil.getUrl(FileUtil.baseDir, generator.filename(baseFilename)), sb.toString());
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
        String link = generator.link(board.name, FileUtil.getUrl(board.id, generator.filename(BoardGenerator.baseFilename)));

        return generator.tableRow(link, generator.escape(board.desc), DateUtil.dateToString(board.dateLastActivity));
    }


}
