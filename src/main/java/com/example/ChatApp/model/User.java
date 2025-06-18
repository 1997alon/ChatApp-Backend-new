package com.example.ChatApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username cannot be blank")
    @Length(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 1, max = 100, message = "Password must be between 1 and 100 characters")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "Name cannot be blank")
    @Length(max = 100, message = "Name must be at most 100 characters")
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Length(max = 100, message = "Email must be at most 100 characters")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    // Constructors
    public User() {}

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
