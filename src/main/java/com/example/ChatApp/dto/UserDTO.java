package com.example.ChatApp.dto;

public class UserDTO {
    private Integer id;
    private String username;
    private String name;

    public UserDTO(Integer id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}