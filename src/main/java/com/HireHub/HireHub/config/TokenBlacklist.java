package com.HireHub.HireHub.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    public void add(String token, long expirationEpochMilli) {
        prune();
        blacklist.put(token, expirationEpochMilli);
    }

    public boolean contains(String token) {
        prune();
        return blacklist.containsKey(token);
    }

    private void prune() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry -> entry.getValue() <= now);
    }
}