package com.github.imrezol.trelloexporter.trello.dto;

import java.time.ZonedDateTime;

public class CheckItem {
    public String id;
    public String name;
    public Object nameData;
    public long pos;
    public String state;
    public ZonedDateTime due;
    public Object dueReminder;
    public String idMember;
    public String idChecklist;

    public boolean isComplete(){
        return "complete".equals(state); // incomplete, complete
    }
}
