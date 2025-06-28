package com.github.imrezol.trelloexporter.trello.dto;

import java.time.ZonedDateTime;

public class Action {
    public String id;
    public String idMemberCreator;
    public Object data; // IZEIZE
    public Object appCreator; // Not used
    public String type;
    public ZonedDateTime date;
    public Object limits; // Not used
    public Object memberCreator; // Not used
}
