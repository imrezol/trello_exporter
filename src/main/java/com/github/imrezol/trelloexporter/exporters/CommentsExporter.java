package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.trello.dto.Action;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class CommentsExporter {


    public static void export(BufferedWriter writer, Card card, List<Action> actions) throws IOException {
        if (card.badges.comments == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder()
                .append(System.lineSeparator())
                .append(new Heading(String.format("Comments (%d):", card.badges.comments), 3)).append(System.lineSeparator())
                .append(System.lineSeparator()).append(new HorizontalRule()).append(System.lineSeparator());



        writer.write(sb.toString());
        actions.stream()
                .filter(Action::isComment)
                .forEach(action -> {
                    generateComment(writer, action, card);
                });

    }

    private static void generateComment(BufferedWriter writer, Action action, Card card)  {
        StringBuilder sb = new StringBuilder()
                .append(System.lineSeparator())
                .append(action.toMd(card.attachments)).append(System.lineSeparator())
                .append(System.lineSeparator()).append(new HorizontalRule()).append(System.lineSeparator());


        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
