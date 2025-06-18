package com.example.ChatApp.Store;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveUserStore {
    private final Map<Integer, WebSocketSession> activeUsers = new ConcurrentHashMap<>();

    public void addUser(Integer userId, WebSocketSession session) {
        activeUsers.put(userId, session);
    }

    public void removeUser(Integer userId) {
        activeUsers.remove(userId);
    }

    public WebSocketSession getSession(Integer userId) {
        return activeUsers.get(userId);
    }
}
