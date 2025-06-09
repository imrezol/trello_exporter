package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardExporter {

    private static final Logger logger = LoggerFactory.getLogger(BoardExporter.class);

    @Autowired
    private TrelloApi trelloApi;

    @Autowired
    private Properties properties;

    @Autowired
    private CardExporter cardExporter;

    public void export(Board board) {

        logger.info("Exporting board:{}", board.name);

        saveToJson(board);

        List<TrelloList> trelloLists = trelloApi.getLists(board.id)
                .stream()
                .filter(trelloList -> !trelloList.closed).toList();

        Path fileName = Paths.get(properties.baseDir, board.id, properties.getBoardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {
            writer.write(getHeader(board));
            writer.newLine();

            writer.write(generateLists(board, trelloLists));
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToJson(Board board) {
        String json = trelloApi.getBoardJson(board.id);
        Utils.saveToFile(Paths.get(properties.baseDir, board.id, properties.getBoardJson()), json);
    }


    private String generateLists(Board board, List<TrelloList> trelloLists){

        Map<String, List<Card>> cardsByList = new HashMap<>();
        for (TrelloList list : trelloLists) {
            String listId = list.id;
            List<Card> cards = trelloApi.getCards(listId);
            cardsByList.put(listId, cards);
        }

        trelloLists.forEach(trelloList -> {
            List<Card> cards = trelloApi.getCards(trelloList.id);
            cardsByList.put(trelloList.id, cards);
        });

        Table.Builder tableBuilder = new Table.Builder()
//                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT)
                .addRow(trelloLists.stream().map(trelloList -> trelloList.name).toArray());

        int rowIndex = 0;
        while (true) {


            List<Object> cells = new ArrayList<>();
            boolean wasCard = false;
            for (TrelloList trelloList : trelloLists) {

                List<Card> cards = cardsByList.get(trelloList.id);
                if (cards.size()>rowIndex) {
                    wasCard = true;
                    Card card = cards.get(rowIndex);
                    cells.add( new Link(card.name, Utils.getUrl(properties.getCardMd(),card.id))) ;
                    cardExporter.export(card);
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


    private String getHeader(Board board) {
        StringBuilder sb = new StringBuilder()
                .append(new Heading("Board: " + board.name, 1)).append("\n")
                .append("Description: " + board.desc).append("\n")
                .append(new Link("<br>\nBack to boards","../Boards.md")).append("\n");

        return sb.toString();
    }
}
