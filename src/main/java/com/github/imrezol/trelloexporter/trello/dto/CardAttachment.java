package com.github.imrezol.trelloexporter.trello.dto;

import org.apache.commons.io.FilenameUtils;

import java.time.ZonedDateTime;
import java.util.List;

public class CardAttachment {
    public String id;
    public int bytes;
    public ZonedDateTime date;
    public String edgeColor;
    public String idMember;
    public boolean isMalicious;
    public boolean isUpload;
    public String mimeType;
    public String name;
    public List<Object> previews; // IZEIZE
    public String url;
    public long pos;
    public String fileName;

    public String getLocalFilename() {
        String ext = FilenameUtils.getExtension(fileName);
        String baseName = FilenameUtils.getBaseName(fileName);

        if (ext.isEmpty()) {
            return baseName + "_" + id;
        } else {
            return baseName + "_" + id + "." + ext;
        }

    }
}
