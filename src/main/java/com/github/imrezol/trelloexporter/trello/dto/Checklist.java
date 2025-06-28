package com.github.imrezol.trelloexporter.trello.dto;

import java.util.List;

public class Checklist {
    public String id;
    public String name;
    public String idBoard;
    public String idCard;
    public long pos;
    public Object limits;
    public List<CheckItem> checkItems;
    public Object creationMethod;

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
