package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckItem {

    public String id;
    public String name;
    public String state;

    public boolean isComplete(){
        return "complete".equals(state); // incomplete, complete
    }
}
