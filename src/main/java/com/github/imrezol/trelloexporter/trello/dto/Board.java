package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.utils.JsonUtil;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;


public class Board {
    public String id;
    public String nodeId;
    public String name;
    public String desc;
    public String descData;
    public boolean closed;
    public ZonedDateTime dateClosed;
    public String idOrganization;
    public String idEnterprise;
    public Object limits; // Not used
    public boolean pinned;
    public boolean starred;
    public String url;
    public Object prefs; // Not used
    public String shortLink;
    public boolean subscribed;
    public Map<String, String> labelNames;
    public List<Object> powerUps; // Not used
    public ZonedDateTime dateLastActivity;
    public ZonedDateTime dateLastView;
    public String shortUrl;
    public List<String> idTags;
    public Object datePluginDisable; // Not used
    public Object creationMethod; // Not used
    public String ixUpdate;
    public Object templateGallery; // Not used
    public boolean enterpriseOwned;
    public String idBoardSource;
    public List<String> premiumFeatures;
    public String idMemberCreator;
    public Object type; // Not used
    public List<Action> actions;
    public List<Card> cards;
    public List<Label> labels;
    public List<TrelloList> lists;
    public List<Member> members;
    public List<Checklist> checklists;
    public List<Object> customFields;
    public List<Membership> memberships;
    public List<Object> pluginData;

    public static Board fromJson(String jsonString) {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Board.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

