package com.example.ChatApp.dto;

import com.example.ChatApp.model.Reply;

public class ReplyDTO {
    private Long id;
    private String senderName;
    private String receiverName;
    private boolean approved;

    public ReplyDTO(Reply reply) {
        this.id = reply.getId();
        this.senderName = reply.getSender().getName();
        this.receiverName = reply.getReceiver().getName();
        this.approved = reply.isApproved();
    }

    // Add these getters:

    public Long getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public boolean isApproved() {
        return approved;
    }
}

