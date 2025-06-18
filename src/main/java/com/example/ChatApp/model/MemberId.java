package com.example.ChatApp.model;

import java.io.Serializable;
import java.util.Objects;

public class MemberId implements Serializable {
    private Integer room;
    private Integer user;

    public MemberId() {}

    public MemberId(Integer room, Integer user) {
        this.room = room;
        this.user = user;
    }

    // equals() and hashCode() are required for composite keys

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId)) return false;
        MemberId that = (MemberId) o;
        return Objects.equals(room, that.room) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, user);
    }
}
