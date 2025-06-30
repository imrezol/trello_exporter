package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.ConsoleUtil;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
import com.github.imrezol.trelloexporter.utils.Builder;
import com.github.imrezol.trelloexporter.utils.FileUtil;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGenerator {

    private Generator generator;
    private Board board;

    public final static String baseFilename = "Board";

    public BoardGenerator(Generator generator, Board board) {
        this.generator = generator;
        this.board = board;
    }

    public void generate() {

        System.out.println(ConsoleUtil.pad(1, "Exporting board: "+ board.name));

        Builder sb = new Builder()
                .append(generator.begin(baseFilename))
                .append(getHeader())
                .append(generateLists())
                .append(generator.end());


        FileUtil.saveToFile(FileUtil.getUrl(FileUtil.baseDir, board.id, generator.filename(baseFilename)), sb.toString());
    }


    private String generateLists() {

        List<TrelloList> activeLists = board.lists.stream()
                .filter(trelloList1 -> !trelloList1.closed)
                .sorted((a, b) -> Long.compare(a.pos, b.pos))
                .toList();

        Map<String, List<Card>> cardsByList = new HashMap<>();

        for (TrelloList list : activeLists) {
            cardsByList.put(list.id, new ArrayList<>());
        }

        for (Card card : board.cards) {
            List<Card> cards = cardsByList.get(card.idList);
            if (cards != null) {
                cards.add(card);
            }
        }

        Builder sb = new Builder();

        sb.append(
                generator.tableHeader(
                        activeLists.stream()
                                .map(trelloList1 -> trelloList1.name)
                                .toArray(String[]::new)
                )
        );

        int rowIndex = 0;
        while (true) {


            List<String> cells = new ArrayList<>();
            boolean wasCard = false;
            for (TrelloList trelloList : activeLists) {

                List<Card> cards = cardsByList.get(trelloList.id).stream()
                        .filter(card -> !card.closed)
                        .sorted((c1, c2) -> c1.pos > c2.pos ? 1 : 0)
                        .toList();
                if (cards.size() > rowIndex) {
                    wasCard = true;
                    Card card = cards.get(rowIndex);
                    String badges = Badges.generateCardBadge(card.badges);
                    String link = generator.link(card.name, FileUtil.getUrl(card.id, generator.filename(CardGenerator.baseFilename))); // IZEIZE
                    if (Strings.isNotBlank(badges)) {
                        cells.add(link + "<br>" + badges);
                    } else {
                        cells.add(link);
                    }

                } else {
                    cells.add("");
                }
            }

            if (wasCard) {
                sb.append(generator.tableRow(cells.toArray(String[]::new)));
            } else {
                break;
            }
            rowIndex++;
        }

        sb.append(generator.tableFoot());

        return sb.toString();

    }


    private String getHeader() {
        Builder sb = new Builder()
                .appendLine( generator.exportedDateProperty())
                .appendLine(generator.link("Back to boards", "../" + generator.filename(BoardsGenerator.baseFilename))) // IZEIZE
                .appendLine(generator.heading("Board: " + board.name, 1));
        if (!Strings.isBlank(board.desc)) {
            sb.appendLine("Description: " + generator.escape(board.desc));
        }

        return sb.toString();
    }
}
