package com.example.ChatApp.dto;

public class MessageSentDTO {
    private Integer roomId;
    private Integer senderId;
    private String content;
    private String type; // "text", "image", "file"

    public MessageSentDTO() {}

    public MessageSentDTO(Integer roomId, Integer senderId, String content, String type) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.type = type;
    }

    // Getters and Setters
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
