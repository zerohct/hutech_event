package com.project.hutech_event.blacklist;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlackList {
    private Set<String> blacklist = new HashSet<>();

    public void addToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

}
