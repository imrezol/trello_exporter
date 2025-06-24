package com.github.imrezol.trelloexporter.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CheckItem;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ChecklistExporter {

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    public void export(BufferedWriter writer, Card card) throws IOException {
        if (ArrayUtils.isEmpty(card.idChecklists)) {
            return;
        }

        Path fileName = Paths.get(properties.baseDir, card.idBoard, card.id, properties.getChecklistsJson());

        String checklistsJson = trelloApi.getChecklists(card.id);
        Utils.saveToFile(fileName, checklistsJson);

        Checklist[] checklists = fromJson(checklistsJson);

        StringBuilder sb = new StringBuilder()
                .append(new Heading(String.format("Checklists (%d):", checklists.length), 3)).append("\n");


        writer.write(sb.toString());

        for (Checklist checklist : checklists) {
            generateChecklist(writer, checklist);
        }

    }

    private void generateChecklist(BufferedWriter writer, Checklist checklist) throws IOException {
        StringBuilder sb = new StringBuilder()
                .append("\n")
                .append(new Heading(String.format("%s (%d/%d)", checklist.name, checklist.getCompletedCount(), checklist.checkItems.length), 4)).append("\n");

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
                    .append(checkItem.name).append("\n");
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
