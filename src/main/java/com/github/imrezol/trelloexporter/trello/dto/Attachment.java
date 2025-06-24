package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {
    public String id;
    public String name;
    public String url;
    public String fileName;

    public int bytes;
    public ZonedDateTime date;
    public boolean isUpload;
}
