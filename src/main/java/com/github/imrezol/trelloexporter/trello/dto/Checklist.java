package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Checklist {

    public  String id;
    public  String name;
    public  String idBoard;
    public  String idCard;
    public  CheckItem[] checkItems;

    public int getCompletedCount() {
        int completed = 0;
        for (CheckItem checkItem : checkItems) {
            if (checkItem.isComplete()) {
                completed++;
            }
        }
        return completed;
    }
}
