package com.example.ChatApp.controller;

import com.example.ChatApp.Store.ActiveUserStore;
import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.LoginRequest;
import com.example.ChatApp.dto.UserDTO;
import com.example.ChatApp.model.User;
import com.example.ChatApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ActiveUserStore activeUserStore;

    @PostMapping("/login")
    public ApiResponse<UserDTO> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            ApiResponse<User> response = userService.login(loginRequest).get();

            if (response.isSuccess() && response.getData() != null) {
                User user = response.getData();
                session.setAttribute("id", user.getId());

                UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getName());
                return new ApiResponse<>(true, "Login successful", dto);
            }
            return new ApiResponse<>(false, response.getMessage(), null);

        } catch (Exception e) {
            logger.error("Login failed for username: " + loginRequest.getUsername(), e);
            return new ApiResponse<>(false, "Login failed: " + e.getMessage(), null);
        }
    }

    @PostMapping("/signup")
    public ApiResponse<User> signup(@Valid @RequestBody User user) {
        try {
            ApiResponse<User> response = userService.signup(user).get();

            if (response.isSuccess()) {
                logger.info("Signup successful for username: {}", user.getUsername());
            } else {
                logger.warn("Signup failed for username: {}. Reason: {}", user.getUsername(), response.getMessage());
            }
            return response;

        } catch (Exception e) {
            logger.error("Signup failed with exception for username: " + user.getUsername(), e);
            String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            return new ApiResponse<>(false, "Signup failed: " + message, null);
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId != null) {
            activeUserStore.removeUser(userId);
            session.invalidate();
            return new ApiResponse<>(true, "Logout successful", null);
        } else {
            return new ApiResponse<>(false, "No active session found", null);
        }
    }

    @GetMapping("/checkName")
    public ApiResponse<Boolean> checkName(@RequestParam("name") String name) {
        try {
            return userService.checkName(name).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt status
            return new ApiResponse<>(false, "Error while fetching names: " + e.getMessage(), null);
        }
    }

}
