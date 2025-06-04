package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrelloList {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;
}
