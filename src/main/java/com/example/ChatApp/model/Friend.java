package com.example.ChatApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Friend")
@IdClass(FriendId.class)
public class Friend {

    @Id
    @ManyToOne
    @JoinColumn(name = "friendOne")
    private User friendOne;

    @Id
    @ManyToOne
    @JoinColumn(name = "friendTwo")
    private User friendTwo;

    // Constructors
    public Friend() {}

    public Friend(User friendOne, User friendTwo) {
        this.friendOne = friendOne;
        this.friendTwo = friendTwo;
    }

    // Getters and Setters
}