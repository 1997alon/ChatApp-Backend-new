package com.example.ChatApp.controller;

import com.example.ChatApp.dto.*;
import com.example.ChatApp.service.FriendService;
import com.example.ChatApp.service.ReplyService;
import com.example.ChatApp.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping("/request")
    public ApiResponse<Void> replyFriend(@RequestBody ReplyFriendRequest request) {
        try {
            return replyService.replyToFriendRequest(request).get();
        } catch (Exception e) {

            return new ApiResponse<>(false, "Failed to reply to friend request");
        }
    }

    // Optional: view pending requests
    @GetMapping("/get")
    public ApiResponse<List<ReplyDTO>> getReplies(@RequestParam String name) {
        try {
            return replyService.getReplies(name).get();
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to get replies");
        }
    }
}
