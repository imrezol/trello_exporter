package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;


public class Card {

    @JsonProperty("id")
    public String id;

    public String idBoard;

    public Badges badges;

    @JsonProperty("name")
    public String name;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("closed")
    public Boolean closed;

    public ZonedDateTime dateLastActivity;
    public ZonedDateTime due;
//    public Boolean dueComplete;
    public String[] idChecklists;

}
