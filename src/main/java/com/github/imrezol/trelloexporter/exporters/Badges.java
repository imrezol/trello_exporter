package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Utils;
import com.github.imrezol.trelloexporter.trello.dto.CardBadges;
import com.github.imrezol.trelloexporter.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class Badges {

    /*
    https://www.shapecatcher.com/
   https://fonts.google.com/icons?selected=Material+Symbols+Outlined:visibility:FILL@0;wght@400;GRAD@0;opsz@20&icon.size=16&icon.color=%23000000&icon.query=eye
     */
    public static String generateCardBadge(CardBadges badge) {
        List<String> badges = new ArrayList<>();

        addWatched(badge, badges);
        addDue(badge, badges);
        addDescription(badge, badges);
        addComments(badge, badges);
        addAttachments(badge, badges);
        addCheks(badge, badges);

        return String.join(" ", badges);
    }

    private static void addCheks(CardBadges badge, List<String> badges) {
        if (badge.checkItems>0){
            // Check Box
            StringBuilder sb = new StringBuilder()
                    .append("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 0 24 24\" width=\"14px\" fill=\"#000000\"><path d=\"M0 0h24v24H0V0z\" fill=\"none\"/><path d=\"M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14zM17.99 9l-1.41-1.42-6.59 6.59-2.58-2.57-1.42 1.41 4 3.99z\"/></svg>")
                    .append(" ")
                    .append(badge.checkItemsChecked)
                    .append("/")
                    .append(badge.checkItems);

            badges.add(sb.toString());
        }
    }

    private static void addAttachments(CardBadges badge, List<String> badges) {
        if (badge.attachments>0){
            // Attach File
            StringBuilder sb = new StringBuilder()
                    .append("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 0 24 24\" width=\"14px\" fill=\"#000000\"><path d=\"M0 0h24v24H0V0z\" fill=\"none\"/><path d=\"M16.5 6v11.5c0 2.21-1.79 4-4 4s-4-1.79-4-4V5c0-1.38 1.12-2.5 2.5-2.5s2.5 1.12 2.5 2.5v10.5c0 .55-.45 1-1 1s-1-.45-1-1V6H10v9.5c0 1.38 1.12 2.5 2.5 2.5s2.5-1.12 2.5-2.5V5c0-2.21-1.79-4-4-4S7 2.79 7 5v12.5c0 3.04 2.46 5.5 5.5 5.5s5.5-2.46 5.5-5.5V6h-1.5z\"/></svg>")
                    .append(" ")
                    .append(badge.attachments);
            badges.add(sb.toString());
        }
    }

    private static void addComments(CardBadges badge, List<String> badges) {
        if (badge.comments>0){
            // Chat Bubble Outline
            StringBuilder sb = new StringBuilder()
                    .append("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 0 24 24\" width=\"14px\" fill=\"#000000\"><path d=\"M0 0h24v24H0V0z\" fill=\"none\"/><path d=\"M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z\"/></svg>")
                    .append(" ")
                    .append(badge.comments);
            badges.add(sb.toString());
        }
    }

    private static void addDescription(CardBadges badge, List<String> badges) {
        if (badge.description){
            // Notes
            badges.add("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 0 24 24\" width=\"14px\" fill=\"#000000\"><path d=\"M0 0h24v24H0V0z\" fill=\"none\"/><path d=\"M21 11.01L3 11v2h18zM3 16h12v2H3zM21 6H3v2.01L21 8z\"/></svg>");
        }
    }

    private static void addDue(CardBadges badge, List<String> badges) {
        if (badge.due != null) {
            // Schedule
            StringBuilder sb = new StringBuilder()
//                    .append("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"20px\" viewBox=\"0 -960 960 960\" width=\"20px\" fill=\"#000000\"><path d=\"m614-310 51-51-149-149v-210h-72v240l170 170ZM480-96q-79.38 0-149.19-30T208.5-208.5Q156-261 126-330.96t-30-149.5Q96-560 126-630q30-70 82.5-122t122.46-82q69.96-30 149.5-30t149.55 30.24q70 30.24 121.79 82.08 51.78 51.84 81.99 121.92Q864-559.68 864-480q0 79.38-30 149.19T752-208.5Q700-156 629.87-126T480-96Zm0-384Zm.48 312q129.47 0 220.5-91.5Q792-351 792-480.48q0-129.47-91.02-220.5Q609.95-792 480.48-792 351-792 259.5-700.98 168-609.95 168-480.48 168-351 259.5-259.5T480.48-168Z\"/></svg>")
                    .append("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 -960 960 960\" width=\"14px\" fill=\"#000000\"><path d=\"m614-310 51-51-149-149v-210h-72v240l170 170ZM480-96q-79.38 0-149.19-30T208.5-208.5Q156-261 126-330.96t-30-149.5Q96-560 126-630q30-70 82.5-122t122.46-82q69.96-30 149.5-30t149.55 30.24q70 30.24 121.79 82.08 51.78 51.84 81.99 121.92Q864-559.68 864-480q0 79.38-30 149.19T752-208.5Q700-156 629.87-126T480-96Zm0-384Zm.48 312q129.47 0 220.5-91.5Q792-351 792-480.48q0-129.47-91.02-220.5Q609.95-792 480.48-792 351-792 259.5-700.98 168-609.95 168-480.48 168-351 259.5-259.5T480.48-168Z\"/></svg>")
                    .append(" ")
                    .append(DateUtil.dueToString(badge.due));
            badges.add(sb.toString());
        }
    }

    private static void addWatched(CardBadges badge, List<String> badges) {
        if (badge.subscribed) {
            // Visibility
            badges.add("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"14px\" viewBox=\"0 -960 960 960\" width=\"14px\" fill=\"#000000\"><path d=\"M480-320q75 0 127.5-52.5T660-500q0-75-52.5-127.5T480-680q-75 0-127.5 52.5T300-500q0 75 52.5 127.5T480-320Zm0-72q-45 0-76.5-31.5T372-500q0-45 31.5-76.5T480-608q45 0 76.5 31.5T588-500q0 45-31.5 76.5T480-392Zm0 192q-146 0-266-81.5T40-500q54-137 174-218.5T480-800q146 0 266 81.5T920-500q-54 137-174 218.5T480-200Zm0-300Zm0 220q113 0 207.5-59.5T832-500q-50-101-144.5-160.5T480-720q-113 0-207.5 59.5T128-500q50 101 144.5 160.5T480-280Z\"/></svg>");
        }
    }
}
