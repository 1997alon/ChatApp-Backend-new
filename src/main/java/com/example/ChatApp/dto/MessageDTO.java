package com.example.ChatApp.dto;

import com.example.ChatApp.model.Message;

import java.time.format.DateTimeFormatter;

public class MessageDTO {
    private Integer id;
    private String content;
    private String senderName;
    private String time;
    private String type;
    private boolean isRead;

    public MessageDTO(Message message, boolean isRead) {
        this.id = message.getId();
        this.content = message.getContent();
        this.senderName = message.getSender().getUsername();

        // Format LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.time = message.getTime().format(formatter);

        this.type = message.getType().name();
        this.isRead = isRead;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public void getSenderName(String senderUsername) {
        this.senderName = senderUsername;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
