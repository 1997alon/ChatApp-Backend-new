package com.example.ChatApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Member")
@IdClass(MemberId.class)
public class Member {

    @Id
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Member() {}

    public Member(Room room, User user) {
        this.room = room;
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
