package com.example.ChatApp.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MessageStatusId implements Serializable {

    private Integer messageId;
    private Integer userId;

    // Default constructor
    public MessageStatusId() {}

    // Parameterized constructor
    public MessageStatusId(Integer messageId, Integer userId) {
        this.messageId = messageId;
        this.userId = userId;
    }

    // Getters
    public Integer getMessageId() {
        return messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    // Setters
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // equals() and hashCode() – required for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageStatusId)) return false;
        MessageStatusId that = (MessageStatusId) o;
        return Objects.equals(messageId, that.messageId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, userId);
    }
}
