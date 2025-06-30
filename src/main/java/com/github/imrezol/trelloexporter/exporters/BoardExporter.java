package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
import com.github.imrezol.trelloexporter.utils.DateUtil;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardExporter {

    private static final Logger logger = LoggerFactory.getLogger(BoardExporter.class);


    public static void export(Board board) {

        System.out.println(String.format(Utils.pad(1,"Exporting board:%s"), board.name));

        String mdDir = Utils.getUrl(board.id, Properties.baseDir);
        Utils.ensureDirectory(mdDir);

        Path fileName = Paths.get( mdDir, Properties.getBoardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {
            writer.write(getHeader(board));
            writer.newLine();

            writer.write(generateLists(board));
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static String generateLists(Board board){

        Map<String, List<Card>> cardsByList = new HashMap<>();

        for (TrelloList list : board.lists) {
            cardsByList.put(list.id, new ArrayList<>());
        }

        for (Card card: board.cards) {
            List<Card> cards = cardsByList.get(card.idList);
            if (cards != null) {
                cards.add(card);
            }
        }

        Table.Builder tableBuilder = new Table.Builder()
                .addRow(board.lists.stream()
                        .filter(trelloList -> !trelloList.closed)
                        .map(trelloList -> trelloList.name ).toArray());

        int rowIndex = 0;
        while (true) {


            List<Object> cells = new ArrayList<>();
            boolean wasCard = false;
            for (TrelloList trelloList : board.lists) {

                List<Card> cards = cardsByList.get(trelloList.id).stream()
                        .filter(card -> !card.closed)
                        .sorted((c1, c2) -> c1.pos>c2.pos?1:0)
                        .toList();
                if (cards.size()>rowIndex) {
                    wasCard = true;
                    Card card = cards.get(rowIndex);
                    String badges = Badges.generateCardBadge(card.badges);
                    Link link = new Link(card.name, Utils.getUrl(Properties.getCardMd(), card.id));
                    if (Strings.isNotBlank(badges)) {
                        cells.add( link + "<br>" + badges) ;
                    } else {
                        cells.add(link) ;
                    }

                } else {
                    cells.add(null);
                }
            }

            if (wasCard) {
                tableBuilder.addRow(cells.toArray());
            } else {
                break;
            }
            rowIndex++;
        }

        return tableBuilder.build().toString();

    }


    private static String getHeader(Board board) {
        StringBuilder sb = new StringBuilder()
                .append("Export date: ").append(DateUtil.dateToStringWithTimeZone(Properties.exportDate)).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Link("Back to boards","../Boards.md")).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Heading("Board: " + board.name, 1)).append(System.lineSeparator());
                if (!Strings.isBlank(board.desc)) {
                    sb.append("Description: " + board.desc).append(System.lineSeparator());
                }

        return sb.toString();
    }
}
