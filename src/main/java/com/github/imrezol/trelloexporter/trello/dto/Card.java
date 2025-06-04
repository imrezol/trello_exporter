package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {

    @JsonProperty("id")
    public String id;

    @JsonProperty("address")
    public String address;

    @JsonProperty("name")
    public String name;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("closed")
    public boolean closed;

    public String dateLastActivity;

}
