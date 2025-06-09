package com.github.imrezol.trelloexporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Properties {

    @Value("${trello.skipArchives: true}")
    public boolean skipArchives;

    public final String baseDir = generateBaseDir();

    private final String boardsFilename = "Boards";
    private final String boardFilename = "Board";
    private final String cardFilename = "Card";

    private String generateBaseDir() {

        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

        return String.format("exports/%s", formatter.format(LocalDateTime.now()));
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

}
