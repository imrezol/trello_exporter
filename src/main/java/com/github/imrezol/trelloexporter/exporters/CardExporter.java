package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CardDetails;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.link.Link;
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


@Service
public class CardExporter {

    private static final Logger logger = LoggerFactory.getLogger(CardExporter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    public void export(Board board, Card card) {
        logger.info("Exporting Card:{}", card.name);

        String cardJson = trelloApi.getCard(card.id);
        saveToJson(board, card.id, cardJson);
        generateMD(board, card);
    }

    private void generateMD(Board board, Card card) {

        Path fileName = Paths.get(properties.baseDir, board.id, card.id, properties.getCardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {

            StringBuilder sb = new StringBuilder()
                    .append(new Heading("Card: " + card.name, 1)).append("\n")
                    .append("Description: " + card.desc).append("\n")
                    .append(new Link("<br>\nBack to boards","../../Boards.md")).append("\n");

            writer.write(sb.toString());
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToJson(Board board, String cardId, String cardJson) {
//        String json = trelloApi.getBoardJson(board.id);
        Utils.saveToFile(Paths.get(properties.baseDir, board.id, cardId, properties.getCardJson()), cardJson);
    }
}
