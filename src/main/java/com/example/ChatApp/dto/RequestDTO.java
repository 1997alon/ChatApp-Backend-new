package com.example.ChatApp.dto;

import com.example.ChatApp.model.Request;
import com.example.ChatApp.model.RequestStatus;

import java.time.LocalDateTime;

public class RequestDTO {
    private Long id; // ✅ Changed from Integer to Long
    private Integer senderId;
    private String senderName;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public RequestDTO(Request request) {
        this.id = request.getId(); // No error now
        this.senderId = request.getSender().getId();
        this.senderName = request.getSender().getName();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
    }

    public Long getId() { return id; }
    public Integer getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public RequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
