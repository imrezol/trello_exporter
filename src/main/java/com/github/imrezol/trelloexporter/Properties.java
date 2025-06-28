package com.github.imrezol.trelloexporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Properties {

    private static final String boardsFilename = "Boards";
    private static final String boardFilename = "Board";
    private static final String cardFilename = "Card";
    public static final String attachmentsDir = "Attachments";

    public static final ZonedDateTime exportDate = ZonedDateTime.now();

    public static final String baseDir = String.format(
            "exports/%s",
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"
            ).format(exportDate));


    public static String getBoardsMd() {
        return boardsFilename + ".md";
    }

    public static String getBoardMd() {
        return boardFilename + ".md";
    }

    public static String getBoardJson() {
        return boardFilename + ".json";
    }

    public static String getCardMd() {
        return cardFilename + ".md";
    }

}
