package com.github.imrezol.trelloexporter.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CheckItem;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;

public class ActionsExporter {

    public void export(BufferedWriter writer, Card card) throws IOException {
        if (card.badges.comments == 0) {
            return;
        }


//        Checklist[] checklists = fromJson(actionsJson);
//
//        StringBuilder sb = new StringBuilder()
//                .append(new Heading(String.format("Checklists (%d):", checklists.length), 3)).append(System.lineSeparator());
//
//
//        writer.write(sb.toString());
//
//        for (Checklist checklist : checklists) {
//            generateChecklist(writer, checklist);
//        }

    }

    private void generateChecklist(BufferedWriter writer, Checklist checklist) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append(System.lineSeparator())
                .append(new Heading(String.format("%s (%d/%d)", checklist.name, checklist.getCompletedCount(), checklist.checkItems.size()), 4)).append(System.lineSeparator());

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
                    .append(checkItem.name).append(System.lineSeparator());
        }

        writer.write(sb.toString());
    }

    private static Checklist[] fromJson(String jsonString) {

        ObjectMapper objectMapper = Utils.getObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Checklist[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
