package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Attachment;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.imrezol.trelloexporter.exporters.AttachmentExporter.fromJson;


@Service
public class CardExporter {

    private static final Logger logger = LoggerFactory.getLogger(CardExporter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    @Autowired
    private ChecklistExporter checklistExporter;

    @Autowired
    private AttachmentExporter attachmentExporter;

    public void export(Board board, Card card) {
        logger.info(Utils.pad(2,"Exporting Card:{}"), card.name);

        String cardJson = trelloApi.getCard(card.id);
        saveToJson(board, card.id, cardJson);
        generateMD(board, card);
    }

    private void generateMD(Board board, Card card) {

        Path fileName = Paths.get(properties.baseDir, board.id, card.id, properties.getCardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {

            generateHeader(writer, board);
            generateCard(writer, card);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateCard(BufferedWriter writer, Card card) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append(new Heading(card.name, 2)).append("\n")
                .append("Last activity: ").append(Utils.dateToString(card.dateLastActivity)).append("\n");

        if (card.due != null) {
            sb.append("<br>").append("\n")
                    .append("Due: " + Utils.dateToString(card.due)).append("\n");
        }

        String attachmentsJson = trelloApi.getAttachments(card.id);
        Attachment[] attachments = fromJson(attachmentsJson);

        if (!Strings.isBlank(card.desc)) {
            sb.append(new Heading("Description:", 3)).append("\n")
                    .append(attachmentExporter.fixAttachments(attachments, Utils.emojisToUtf8(card.desc))).append("\n");
        }

        writer.write(sb.toString());

        checklistExporter.export(writer, card);

        attachmentExporter.export(writer, card, attachmentsJson, attachments);
    }


    private void generateHeader(BufferedWriter writer, Board board) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append("Export date: " + Utils.dateToStringWithTimeZone(properties.exportDate)).append("\n")
                .append("<br>").append("\n")
                .append(new Link("Back to boards", "../../Boards.md")).append("\n")
                .append("<br>").append("\n")
                .append(new Link("Back to board " + board.name, "../Board.md")).append("\n");

        writer.write(sb.toString());
        writer.newLine();
    }

    private void saveToJson(Board board, String cardId, String cardJson) {
        Utils.saveToFile(Paths.get(properties.baseDir, board.id, cardId, properties.getCardJson()), cardJson);
    }
}
