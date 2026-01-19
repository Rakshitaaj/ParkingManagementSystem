package com.ey.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class LoggedOutTokenStore {

    private final Set<String> tokens =ConcurrentHashMap.newKeySet();
    public void add(String token) {
        tokens.add(token);
    }
    public boolean contains(String token) {
        return tokens.contains(token);
    }
}
