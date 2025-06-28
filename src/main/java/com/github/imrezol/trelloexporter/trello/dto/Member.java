package com.github.imrezol.trelloexporter.trello.dto;

import java.util.List;

public class Member {
    public String id;
    public String aaId;
    public boolean activityBlocked;
    public String avatarHash;
    public String avatarUrl;
    public String bio;
    public Object bioData;
    public boolean confirmed;
    public String fullName;
    public Object idEnterprise;
    public List<Object> idEnterprisesDeactivated;
    public Object idMemberReferrer;
    public List<Object> idPremOrgsAdmin;
    public String initials;
    public String memberType;
    public NonPublic nonPublic;
    public boolean nonPublicAvailable;
    public List<Object> products;
    public String url;
    public String username;
    public String status;

    public static class NonPublic {
        public String fullName;
        public String initials;
        public Object avatarHash;
    }
}
