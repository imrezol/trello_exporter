package com.github.imrezol.trelloexporter.exporters;

public abstract class FileNameGenerator {

    public abstract String getBoardsFilename();
    public abstract String getBoardFilename();

    protected final String baseBoardsFilename = "Boards";
    protected final String baseBoardFilename = "Board";
    protected final String baseCardFilename = "Card";
    protected final String baseCardActionsFilename = "CardActions";

}
