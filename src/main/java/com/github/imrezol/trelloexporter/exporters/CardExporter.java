package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.generator.Badges;
import com.github.imrezol.trelloexporter.trello.dto.Action;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
import com.github.imrezol.trelloexporter.utils.DateUtil;
import com.github.imrezol.trelloexporter.utils.FileUtil;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class CardExporter {

    public final static String baseFilename = "Card";


    public static void export(String boardName, Card card, List<Checklist> checklists, String listName, List<Action> actions) {
        System.out.println(String.format(Utils.pad(2, "Exporting Card:%s"), card.name));

        String cardDir = Utils.getUrl(card.id, Properties.baseDir, card.idBoard);
        FileUtil.ensureDirectory(cardDir);

        Path fileName = Paths.get(cardDir, Properties.getCardMd());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString(), true))) {

            generateHeader(writer, boardName);
            generateCard(writer, card, listName);
            ChecklistExporter.export(writer, checklists);

            AttachmentExporter.export(writer,card);

            CommentsExporter.export(writer, card, actions);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void generateCard(BufferedWriter writer, Card card, String listName) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append(new Heading(card.name, 2)).append(System.lineSeparator());

        String badge = Badges.generateCardBadge(card.badges);
        if (!Strings.isBlank(badge)) {
            sb.append(badge)
                    .append("<br>").append(System.lineSeparator())
                    .append("<br>").append(System.lineSeparator());
        }

        sb.append("List: ").append(listName).append("<br>").append(System.lineSeparator());
        sb.append("Last activity: ").append(DateUtil.dateToString(card.dateLastActivity)).append("<br>").append(System.lineSeparator());

        if (card.due != null) {
            sb.append("Due: " + DateUtil.dateToString(card.due)).append(System.lineSeparator());
        }


        if (!Strings.isBlank(card.desc)) {
            sb.append(new Heading("Description:", 3)).append(System.lineSeparator());

            String fixedDesc = CardDescriptionFixer.fixMarkDown(card.desc);
            fixedDesc = CardDescriptionFixer.fixAttachments(fixedDesc, card.attachments);
            sb.append(fixedDesc).append(System.lineSeparator());
        }

        writer.write(sb.toString());
    }


    private static void generateHeader(BufferedWriter writer, String boardName) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append("Export date: " + DateUtil.dateToStringWithTimeZone(Properties.exportDate)).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Link("Back to boards", "../../Boards.md")).append(System.lineSeparator())
                .append("<br>").append(System.lineSeparator())
                .append(new Link("Back to board " + boardName, "../Board.md")).append(System.lineSeparator());

        writer.write(sb.toString());
        writer.newLine();
    }

}
