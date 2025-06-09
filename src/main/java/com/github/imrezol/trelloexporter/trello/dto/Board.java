package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Board {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("closed")
    public boolean closed;

}
