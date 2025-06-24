package com.github.imrezol.trelloexporter.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Attachment;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AttachmentExporter {

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    public void export(BufferedWriter writer, Card card, String attachmentsJson, Attachment[] attachments) throws IOException {
        if (card.badges.attachments==0) {
            return;
        }

        String attachmentsDir = Paths.get(properties.baseDir, card.idBoard, card.id, "Attachments").toString();
        Utils.ensureDirectory(attachmentsDir);
        Path fileName = Paths.get(properties.baseDir, card.idBoard, card.id, properties.getAttachmentsJson());

        Utils.saveToFile(fileName, attachmentsJson);


        StringBuilder sb = new StringBuilder()
                .append("\n")
                .append(new Heading(String.format("Attachments (%d):", attachments.length), 3)).append("\n");


        writer.write(sb.toString());

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_RIGHT)
                .addRow("Name", "Filename","Date", "Size");

        for (Attachment attachment : attachments) {
            tableBuilder.addRow(new Link(attachment.name, attachment.url), attachment.fileName, Utils.dateToString(attachment.date), FileUtils.byteCountToDisplaySize(attachment.bytes));
        }

        writer.write(tableBuilder.build().toString());
        writer.newLine();

    }

    public static Attachment[] fromJson(String jsonString) {

        ObjectMapper objectMapper = Utils.getObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Attachment[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
