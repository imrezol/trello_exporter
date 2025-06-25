package com.github.imrezol.trelloexporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Properties {

    @Value("${trello.skipArchives: true}")
    public boolean skipArchives;

    public final ZonedDateTime exportDate = ZonedDateTime.now();

    public final String baseDir = String.format(
            "exports/%s",
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"
            ).format(exportDate));


    private final String boardsFilename = "Boards";
    private final String boardFilename = "Board";
    private final String cardFilename = "Card";
    private final String checklistsFilename = "Checklists";
    public final String attachmentsDir = "Attachments";


    public String getBoardsMd() {
        return boardsFilename + ".md";
    }

    public String getBoardMd() {
        return boardFilename + ".md";
    }

    public String getBoardJson() {
        return boardFilename + ".json";
    }

    public String getCardMd() {
        return cardFilename + ".md";
    }

    public String getCardJson() {
        return cardFilename + ".json";
    }

    public String getChecklistsJson() {
        return checklistsFilename + ".json";
    }

    public String getAttachmentsJson() {
        return "Attachments.json";
    }
}
