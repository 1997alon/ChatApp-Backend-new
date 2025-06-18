package com.example.ChatApp.model;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Integer friendOne;
    private Integer friendTwo;

    public FriendId() {}

    public FriendId(Integer friendOne, Integer friendTwo) {
        this.friendOne = friendOne;
        this.friendTwo = friendTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendId)) return false;
        FriendId that = (FriendId) o;
        return Objects.equals(friendOne, that.friendOne) && Objects.equals(friendTwo, that.friendTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendOne, friendTwo);
    }
}
