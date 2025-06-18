package com.example.ChatApp.dto;

import java.util.List;

public class NewRoomRequest {
    private String name;
    private Boolean isGroup;
    private List<String> names;
    private String createdBy;

    public NewRoomRequest() {}

    public NewRoomRequest(String name, Boolean isGroup, List<String> names, String createdBy) {
        this.createdBy = createdBy;
        this.name = name;
        this.isGroup = isGroup;
        this.names = names;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public List<String> getNames() {
        return names;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
