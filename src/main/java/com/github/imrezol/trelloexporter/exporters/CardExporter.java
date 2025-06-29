package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
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
import java.util.List;


@Service
public class CardExporter {

    private static final Logger logger = LoggerFactory.getLogger(CardExporter.class);


    @Autowired
    private ChecklistExporter checklistExporter;

    @Autowired
    private AttachmentExporter attachmentExporter;

    @Autowired
    private ActionsExporter actionsExporter;


    public void export(String boardName, Card card, List<Checklist> checklists) {
        System.out.println(String.format(Utils.pad(2, "Exporting Card:%s"), card.name));

        String cardDir = Utils.getUrl(card.id, Properties.baseDir, card.idBoard);
        Utils.ensureDirectory(cardDir);

        Path fileName = Paths.get(cardDir, Properties.getCardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {

            generateHeader(writer, boardName);
            generateCard(writer, card);
            checklistExporter.export(writer, checklists);

            attachmentExporter.export(writer, card);

// IZEIZE            actionsExporter.export(writer, card);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateCard(BufferedWriter writer, Card card) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append(new Heading(card.name, 2)).append(System.lineSeparator());

        String badge = Badges.generateCardBadge(card.badges);
        if (!Strings.isBlank(badge)) {
            sb.append(badge)
                    .append("<br>").append(System.lineSeparator())
                    .append("<br>").append(System.lineSeparator());
        }

        sb.append("Last activity: ").append(Utils.dateToString(card.dateLastActivity)).append(System.lineSeparator());

        if (card.due != null) {
            sb.append("<br>").append(System.lineSeparator())
                    .append("Due: " + Utils.dateToString(card.due)).append(System.lineSeparator());
        }


        if (!Strings.isBlank(card.desc)) {
            sb.append(new Heading("Description:", 3)).append(System.lineSeparator());

            String fixedDesc = CardDescriptionFixer.fixMarkDown(card.desc);
            fixedDesc = CardDescriptionFixer.fixAttachments(fixedDesc, card.attachments);
            sb.append(fixedDesc).append(System.lineSeparator());
        }

        writer.write(sb.toString());
    }


    private void generateHeader(BufferedWriter writer, String boardName) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append("Export date: " + Utils.dateToStringWithTimeZone(Properties.exportDate)).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Link("Back to boards", "../../Boards.md")).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Link("Back to board " + boardName, "../Board.md")).append(System.lineSeparator());

        writer.write(sb.toString());
        writer.newLine();
    }

}
