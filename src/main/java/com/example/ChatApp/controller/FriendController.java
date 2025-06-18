package com.example.ChatApp.controller;


import com.example.ChatApp.dto.AddFriendRequest;
import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.ReplyFriendRequest;
import com.example.ChatApp.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/friend")
public class FriendController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @GetMapping("/get")
    public ApiResponse<List<String>> replyFriend(@RequestParam("name") String name) {
        try {
            return friendService.getFriends(name).get();
        } catch (Exception e) {
            logger.error("Error getting friends", e);
            return new ApiResponse<>(false, "Error getting friends");
        }
    }

}
