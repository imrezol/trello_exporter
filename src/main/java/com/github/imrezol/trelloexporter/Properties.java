package com.github.imrezol.trelloexporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Properties {

    @Value("${trello.skipArchives: true}")
    public boolean skipArchives;

    public ZonedDateTime exportDate = ZonedDateTime.now();

    public final String baseDir = generateBaseDir();


    private final String boardsFilename = "Boards";
    private final String boardFilename = "Board";
    private final String cardFilename = "Card";
    private final String checklistsFilename = "Checklists";

    private String generateBaseDir() {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

        return String.format("exports/%s", formatter.format(exportDate));
    }

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
}
