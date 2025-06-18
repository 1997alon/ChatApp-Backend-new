package com.example.ChatApp.dto;

public class ReplyFriendRequest {
    private String senderName;
    private String receiverName;
    private boolean approved;  // true = approved, false = rejected

    // Default constructor
    public ReplyFriendRequest() {
    }

    // All-args constructor
    public ReplyFriendRequest(String senderName, String receiverName, boolean approved) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.approved = approved;
    }

    // Getters and Setters
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
