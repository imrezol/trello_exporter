package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.trello.dto.Action;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import java.nio.file.Path;
import java.util.List;

public class ActionsMdGenerator extends ActionsGenerator {

    public ActionsMdGenerator(Card card, List<Action> actions) {
        super(card, actions);
    }

    @Override
    public String getFilename() {
        return Path.of( baseFilename + ".md").toString();
    }

    @Override
    public String generateHeader() {
        return new StringBuilder()
                .append(System.lineSeparator())
                .append(new Heading(String.format("Actions (%d):", actions.size()), 3)).append(System.lineSeparator())
                .append(System.lineSeparator()).append(new HorizontalRule()).append(System.lineSeparator())
                .toString();

    }

    @Override
    public String generateAction(Action action) {
        return new StringBuilder()
                .append(System.lineSeparator())
                .append(action.toMd(card.attachments)).append(System.lineSeparator())
                .append(System.lineSeparator()).append(new HorizontalRule()).append(System.lineSeparator())
                .toString();
    }

}
