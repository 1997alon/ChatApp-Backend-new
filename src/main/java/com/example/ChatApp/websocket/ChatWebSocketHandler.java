package com.example.ChatApp.websocket;

import com.example.ChatApp.Store.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ActiveUserStore activeUserStore;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userIdStr = getUserIdFromSession(session);
        if (userIdStr != null) {
            Integer userId = Integer.parseInt(userIdStr);
            activeUserStore.addUser(userId, session);
            System.out.println("User " + userId + " connected via WebSocket.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userIdStr = getUserIdFromSession(session);
        if (userIdStr != null) {
            Integer userId = Integer.parseInt(userIdStr);
            activeUserStore.removeUser(userId);
            System.out.println("User " + userId + " disconnected.");
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery(); // e.g. "userId=123"
        if (query != null && query.startsWith("id=")) {
            return query.substring("id=".length());
        }
        return null;
    }
}