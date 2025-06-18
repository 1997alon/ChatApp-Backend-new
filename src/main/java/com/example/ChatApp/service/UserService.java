package com.example.ChatApp.service;

import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.LoginRequest;
import com.example.ChatApp.model.User;
import com.example.ChatApp.repository.UserRepository;
import com.example.ChatApp.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompletableFuture<ApiResponse<User>> login(LoginRequest loginRequest) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
            if (userOpt.isEmpty()) {
                return new ApiResponse<User>(false, "Invalid username");
            }
            User user = userOpt.get();
            if (PasswordUtils.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                return new ApiResponse<User>(true, "Login successful", user);
            }
            return new ApiResponse<User>(false, "Invalid password");
        }, executor).exceptionally(ex ->
                new ApiResponse<User>(false, "Login failed: " + ex.getMessage(), null)
        );
    }

    public CompletableFuture<ApiResponse<User>> signup(User user) {
        return CompletableFuture.supplyAsync(() -> {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return new ApiResponse<>(false, "Username already exists");
            }

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new ApiResponse<>(false, "Email already exists");
            }

            if (userRepository.findByName(user.getName()).isPresent()) {
                return new ApiResponse<>(false, "Name already exists");
            }
            user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
            User savedUser = userRepository.save(user);
            return new ApiResponse<>(true, "Signup successful");
        }, executor);
    }

    public CompletableFuture<ApiResponse<Boolean>> checkName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<User> userOpt = userRepository.findByName(name);
            if (userOpt.isEmpty()) {
                return new ApiResponse<>(true, "Name not found", false);
            }
            return new ApiResponse<>(true, "Names retrieved", true);
        }, executor);
    }

}
