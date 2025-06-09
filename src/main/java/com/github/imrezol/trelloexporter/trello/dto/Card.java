package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static Card fromJson(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Card.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
