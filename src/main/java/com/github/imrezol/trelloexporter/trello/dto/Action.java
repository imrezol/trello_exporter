package com.github.imrezol.trelloexporter.trello.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.exporters.CardDescriptionFixer;
import com.github.imrezol.trelloexporter.utils.DateUtil;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/*
https://developer.atlassian.com/cloud/trello/guides/rest-api/action-types/
 */
public class Action {
    public String id;
    public String idMemberCreator;
    public ActionData data;
    public Object appCreator; // Not used
    public String type;
    public ZonedDateTime date;
    public Object limits; // Not used
    public MemberCreator memberCreator; // Not used

    public boolean isComment(){
        return "commentCard".equals(type);
    }

    public boolean isCardRelated(){
        return data.card != null;
    }
    public boolean isBoardRelated(){
        return data.board != null;
    }

    public String toMd(List<CardAttachment> attachemnts) {
        StringBuilder sb = new StringBuilder()
                .append(new BoldText("Date:")).append(" "+ DateUtil.dateToString(date)).append("<br>").append(System.lineSeparator())
                .append(new BoldText("User:")).append(String.format(" %s (%s)",memberCreator.fullName, memberCreator.username)).append("<br>").append(System.lineSeparator());;

        switch (type) {
            case "commentCard":
                String fixedMarkDown = CardDescriptionFixer.fixMarkDown(data.text);
                fixedMarkDown = CardDescriptionFixer.fixAttachments(fixedMarkDown, attachemnts);
                sb.append(fixedMarkDown);
                break;
            default:
                try {
                    ObjectMapper objectMapper = Utils.getObjectMapper();
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
                    sb.append(new CodeBlock(s));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
        }
        return sb.toString();
    }
}
