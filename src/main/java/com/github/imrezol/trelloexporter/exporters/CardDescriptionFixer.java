package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.generator.CardGenerator;
import com.github.imrezol.trelloexporter.trello.dto.CardAttachment;
import com.vdurmont.emoji.EmojiParser;

import java.util.List;

public class CardDescriptionFixer {
    public static String fixMarkDown(String desc) {
        String fixedDesc = emojisToUtf8(desc);

        fixedDesc = fixNewLines(fixedDesc);

        return fixedDesc;
    }

    public static String emojisToUtf8(String str) {
        return EmojiParser.parseToUnicode(str);
    }

    public static String fixNewLines(String desc) {
        return desc
                .replace("\n\n\n", "<br><br>") // IZEIZE
                .replace("\n", "<br>");
    }

    public static String fixAttachments(String desc, List<CardAttachment> attachments) {
        String fixedDesc = desc;
        for (CardAttachment attachment : attachments) {
            fixedDesc = fixedDesc.replace(attachment.url, Utils.getUrl(attachment.getLocalFilename(), CardGenerator.attachmentsDir));
        }
        return fixedDesc;
    }
}
