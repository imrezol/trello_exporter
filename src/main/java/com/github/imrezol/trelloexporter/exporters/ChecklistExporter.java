package com.github.imrezol.trelloexporter.exporters;


import com.github.imrezol.trelloexporter.trello.dto.CheckItem;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ChecklistExporter {


    public static void export(BufferedWriter writer, List<Checklist> checklists) throws IOException {
        if (checklists == null || checklists.isEmpty()) {
            return;
        }


        StringBuilder sb = new StringBuilder()
                .append(new Heading(String.format("Checklists (%d):", checklists.size()), 3)).append(System.lineSeparator());


        writer.write(sb.toString());

        for (Checklist checklist : checklists) {
            generateChecklist(writer, checklist);
        }

    }

    private static void generateChecklist(BufferedWriter writer, Checklist checklist) throws IOException {
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
}
