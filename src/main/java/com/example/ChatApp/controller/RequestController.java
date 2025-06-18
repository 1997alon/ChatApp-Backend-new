package com.example.ChatApp.controller;

import com.example.ChatApp.dto.AddFriendRequest;
import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.RequestDTO;
import com.example.ChatApp.service.FriendService;
import com.example.ChatApp.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/add")
    public ApiResponse<Void> addFriend(@RequestBody AddFriendRequest request) {
        try {
            return requestService.addFriendRequest(request).get();
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to add friend request");
        }
    }

    // Optional: view pending requests
    @GetMapping("/pending")
    public ApiResponse<List<RequestDTO>> getPendingRequests(@RequestParam String name) {
        try {
            return requestService.pendingRequests(name).get();
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to get pending requests");
        }
    }
}
