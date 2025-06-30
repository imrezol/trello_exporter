package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Action;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.utils.FileUtil;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class ActionsGenerator {

    protected final Card card;
    protected final List<Action> actions;

    private BufferedWriter writer;


    public abstract String getFilename();
    public abstract String generateHeader();
    public abstract String generateAction(Action action);
    public final String baseFilename = "Actions";

    protected ActionsGenerator(Card card, List<Action> actions) {
        this.card = card;
        this.actions = actions;
    }


    public void generate() {
        if (actions == null || actions.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(generateHeader());

        for (Action action : actions) {
            sb.append(generateAction(action));
        }

        FileUtil.saveToFile(getFilename(), sb.toString());

    }

}
