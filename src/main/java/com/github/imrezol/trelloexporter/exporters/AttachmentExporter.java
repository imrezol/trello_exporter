package com.github.imrezol.trelloexporter.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.Attachment;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.ApiProperties;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import okhttp3.OkHttpClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AttachmentExporter {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentExporter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    @Autowired
    private ApiProperties apiProperties;

    private static final OkHttpClient client = new OkHttpClient();

    public void export(BufferedWriter writer, Card card, String attachmentsJson, Attachment[] attachments) throws IOException {
        if (card.badges.attachments==0) {
            return;
        }

        String attachmentsDir = Paths.get(properties.baseDir, card.idBoard, card.id, properties.attachmentsDir).toString();
        Utils.ensureDirectory(attachmentsDir);
        Path jsonFileName = Paths.get(properties.baseDir, card.idBoard, card.id, properties.getAttachmentsJson());

        Utils.saveToFile(jsonFileName, attachmentsJson);


        StringBuilder sb = new StringBuilder()
                .append("\n")
                .append(new Heading(String.format("Attachments (%d):", attachments.length), 3)).append("\n");


        writer.write(sb.toString());

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_RIGHT)
                .addRow("Name", "Filename","Date", "Size");

        for (Attachment attachment : attachments) {
            tableBuilder.addRow(new Link(attachment.name, getAttachmentRelativeLocation(attachment)), attachment.fileName, Utils.dateToString(attachment.date), FileUtils.byteCountToDisplaySize(attachment.bytes));

            logger.info(Utils.pad(3,"Downloading attachment:{}"), attachment.fileName);

            String newFileName = Utils.getUrl(getAttachmentRelativeLocation(attachment), properties.baseDir, card.idBoard, card.id);

            trelloApi.downloadAttachment(card,attachment, newFileName);
        }

        writer.write(tableBuilder.build().toString());
        writer.newLine();

    }

    public  String fixAttachments(Attachment[] attachments, String string) {
        String fixedDesc = string;
        for (Attachment attachment : attachments) {
            fixedDesc = fixedDesc.replace(attachment.url, getAttachmentRelativeLocation(attachment));
        }
        return fixedDesc;
    }

    private  String getAttachmentRelativeLocation(Attachment attachment) {
        return Utils.getUrl(attachment.fileName,properties.attachmentsDir, attachment.id);
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
