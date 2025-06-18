package com.example.ChatApp.dto;

import com.example.ChatApp.model.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomDTO {
    private Integer id;
    private String name;
    private Boolean isGroup;
    private String time;         // formatted time as String
    private String createdBy;
    private Integer numberUnreadMsg;

    // Constructor
    public RoomDTO(Room room, Integer numberUnreadMsg) {
        this.id = room.getId();
        this.name = room.getName();
        this.isGroup = room.getIsGroup();
        LocalDateTime t = room.getTime();
        this.time = (t != null) ? t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        this.createdBy = room.getCreatedBy().getName();
        this.numberUnreadMsg = numberUnreadMsg;
    }

    // Getters and setters (optional if using Lombok or serialization libraries)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedByName(String createdByName) {
        this.createdBy = createdByName;
    }

    public Integer getNumberUnreadMsg() {
        return numberUnreadMsg;
    }

    public void setNumberUnreadMsg(Integer numberUnreadMsg) {
        this.numberUnreadMsg = numberUnreadMsg;
    }
}
