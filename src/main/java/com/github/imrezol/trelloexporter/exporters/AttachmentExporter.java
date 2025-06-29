package com.github.imrezol.trelloexporter.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.CardAttachment;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import okhttp3.OkHttpClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class AttachmentExporter {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentExporter.class);


    private static final OkHttpClient client = new OkHttpClient();

    public static void export(BufferedWriter writer, Card card) throws IOException {
        if (card.attachments.isEmpty()) {
            return;
        }

        String attachmentsDir = Paths.get(Properties.baseDir, card.idBoard, card.id, Properties.attachmentsDir).toString();
        Utils.ensureDirectory(attachmentsDir);


        StringBuilder sb = new StringBuilder()
                .append(System.lineSeparator())
                .append(new Heading(String.format("Attachments (%d):", card.attachments.size()), 3)).append(System.lineSeparator());


        writer.write(sb.toString());

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_RIGHT)
                .addRow("Name", "Filename","Date", "Size");

        for (CardAttachment attachment : card.attachments) {
            tableBuilder.addRow(new Link(attachment.name, Utils.getUrl(attachment.getLocalFilename(), Properties.attachmentsDir)), attachment.fileName, Utils.dateToString(attachment.date), FileUtils.byteCountToDisplaySize(attachment.bytes));
        }

        writer.write(tableBuilder.build().toString());
        writer.newLine();

    }


    public static CardAttachment[] fromJson(String jsonString) {

        ObjectMapper objectMapper = Utils.getObjectMapper();
        try {
            return objectMapper.readValue(jsonString, CardAttachment[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
