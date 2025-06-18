package com.example.ChatApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    private boolean approved;  // true = approved, false = rejected

    public Reply() {}

    public Reply(User sender, User receiver, boolean approved) {
        this.sender = sender;
        this.receiver = receiver;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
