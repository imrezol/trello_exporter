package com.github.imrezol.trelloexporter.generator;

import com.github.imrezol.trelloexporter.utils.*;
import com.github.imrezol.trelloexporter.exporters.CardDescriptionFixer;
import com.github.imrezol.trelloexporter.trello.dto.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;

import java.nio.file.Paths;
import java.util.List;


public class CardGenerator {

    public  final static String baseFilename = "Card";
    public  final static String attachmentsDir = "Attachments";

    private final Generator generator;
    private final Card card;
    private final String boardName;
    private final String listName;
    private final List<Checklist> checklists;
    private final List<Action> actions;

    public CardGenerator(Generator generator, Card card, String boardName, String listName, List<Checklist> checklists, List<Action> actions) {
        this.generator = generator;
        this.card = card;
        this.boardName = boardName;
        this.listName = listName;
        this.checklists = checklists;
        this.actions = actions;
    }

    public void generate() {
        System.out.println(String.format(ConsoleUtil.pad(2, "Exporting Card:%s"), card.name));

        Builder sb = new Builder()
                .append(generator.begin(baseFilename))
                .append(generateHeader(boardName))
                .append(generateCard())
                .append(generateChecklists())
                .append(generateAttachments())
                .append(generateComments())
                .append(generator.end());


        FileUtil.saveToFile(FileUtil.getUrl(FileUtil.baseDir, card.idBoard, card.id, generator.filename(baseFilename)), sb.toString());
    }

    private String generateCard() {
        Builder sb = new Builder()
                .appendLine(generator.heading(card.name, 2));

        String badge = Badges.generateCardBadge(card.badges);
        if (!Strings.isBlank(badge)) {
            sb.appendLine(badge);
        }

        sb.appendLine(generator.property("List", listName));
        sb.appendLine(generator.property("Last activity", card.dateLastActivity));

        if (card.due != null) {
            sb.appendLine(generator.property("Due: ", card.due));
        }

        if (!Strings.isBlank(card.desc)) {
            sb.appendLine(generator.heading("Description:", 3));

            String fixedDesc = CardDescriptionFixer.fixMarkDown(card.desc);
            fixedDesc = CardDescriptionFixer.fixAttachments(fixedDesc, card.attachments);
            sb.appendLine(generator.md(fixedDesc));
        }

        return sb.toString();
    }


    private String generateHeader(String boardName) {
        return new Builder()
                .appendLine(generator.exportedDateProperty())
                .appendLine(generator.link("Back to boards", "../../" + generator.filename(BoardsGenerator.baseFilename))) // IZEIZE
                .appendLine(generator.link("Back to board " + boardName, "../" + generator.filename(BoardGenerator.baseFilename))) // IZEIZE
                .toString();

    }

    public String generateChecklists() {
        if (checklists == null || checklists.isEmpty()) {
            return "";
        }


        Builder sb = new Builder()
                .appendLine(generator.heading(("Checklists (" + checklists.size() + "):"), 3));


        for (Checklist checklist : checklists) {
            sb.append(generateChecklist(checklist));
        }

        return sb.toString();
    }

    private String generateChecklist(Checklist checklist) {
        String heading = checklist.name + " (" + checklist.getCompletedCount() + "/" + checklist.checkItems.size() + ")";
        Builder sb = new Builder()
                .appendNewLine()
                .append(generator.heading(heading, 4));

/*
- [ ] Task 1
- [x] Task 2
 */
        for (CheckItem checkItem : checklist.checkItems) {
            sb.append("- [");
            if (checkItem.isComplete()) {
                sb.append("x");
            } else {
                sb.append(" ");
            }
            sb.append("] ")
                    .appendLine(checkItem.name);
        }

        return sb.toString();
    }

    public String generateAttachments() {
        if (card.attachments.isEmpty()) {
            return "";
        }

        String attachmentsDir = Paths.get(FileUtil.baseDir, card.idBoard, card.id, CardGenerator.attachmentsDir).toString();
        FileUtil.ensureDirectory(attachmentsDir);


        Builder sb = new Builder()
                .appendLine(generator.heading("Attachments (" + card.attachments.size() + "):", 3));

        sb.append(generator.tableHeader("Name", "Filename", "Date", "Size"));

        for (CardAttachment attachment : card.attachments) {
            sb.append(
                    generator.tableRow(
                            generator.linkNewTab(attachment.name, FileUtil.getUrl(CardGenerator.attachmentsDir, attachment.getLocalFilename())),
                            attachment.fileName,
                            DateUtil.dateToString(attachment.date),
                            FileUtils.byteCountToDisplaySize(attachment.bytes)
                    )
            );

        }

        sb.append(generator.tableFoot());

        return sb.toString();

    }

    public String generateComments() {
        if (card.badges.comments == 0) {
            return "";
        }

        Builder sb = new Builder()
                .append(System.lineSeparator())
                .append(generator.heading(String.format("Comments (" + card.badges.comments + "):"), 3))
                .append(generator.rule());

        actions.stream()
                .filter(Action::isComment)
                .forEach(action -> {
                    sb.append(generateComment(action));
                });

        return sb.toString();
    }

    private String generateComment(Action action) {
        return new Builder()
                .append(generator.md(action.toMd(card.attachments)))
                .append(generator.rule())
                .toString();
    }

}
