package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

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
    public Boolean closed;

    public ZonedDateTime dateLastActivity;
    public ZonedDateTime due;
    public Boolean dueComplete;
    public String[] idChecklists;

    public static Card fromJson(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Card.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
