package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDetails {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;


    @JsonProperty("closed")
    public boolean closed;

    public String dateLastActivity;

}
