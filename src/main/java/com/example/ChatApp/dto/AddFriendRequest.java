package com.example.ChatApp.dto;

public class AddFriendRequest {
    String name;
    String friendName;

    public AddFriendRequest() {}

    public AddFriendRequest(String name, String friendName) {
        this.name = name;
        this.friendName = friendName;
    }

    public String getFriendName() {
        return this.friendName;
    }

    public String getName() {
        return this.name;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setName(String name) {
        this.name = name;
    }

}
