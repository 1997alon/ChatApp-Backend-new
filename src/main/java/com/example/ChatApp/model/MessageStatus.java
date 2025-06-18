package com.example.ChatApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message_status")
public class MessageStatus {

    @EmbeddedId
    private MessageStatusId id;

    @MapsId("messageId")
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // Default constructor
    public MessageStatus() {}

    // Constructor with parameters
    public MessageStatus(Message message, User user, Room room, Boolean isRead) {
        this.message = message;
        this.user = user;
        this.room = room;
        this.isRead = isRead;
        this.id = new MessageStatusId(message.getId(), user.getId());
    }

    // Getters and Setters
    public MessageStatusId getId() {
        return id;
    }

    public void setId(MessageStatusId id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
