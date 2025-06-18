package com.example.ChatApp.controller;


import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.MessageDTO;
import com.example.ChatApp.dto.MessageSentDTO;
import com.example.ChatApp.dto.RoomDTO;
import com.example.ChatApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/get")
    public ApiResponse<List<MessageDTO>> getMessages(@RequestParam("userId") Integer userId, @RequestParam("roomId") Integer roomId) {
        try {
            // Wait for the async service call to complete
            ApiResponse<List<MessageDTO>> response = messageService.getMessages(userId, roomId).get();
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to fetch messages: " + e.getMessage(), null);
        }
    }

    @PostMapping("/new")
    public ApiResponse<List<MessageDTO>> newMessage(@RequestBody MessageSentDTO newMsg) {
        try {
            ApiResponse<List<MessageDTO>> response = messageService.sendNewMessage(newMsg).get();
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to fetch messages: " + e.getMessage(), null);
        }
    }

    @GetMapping("/seen")
    public ApiResponse<Void> seenMessages(
            @RequestParam("userId") Integer userId,
            @RequestParam("roomId") Integer roomId) {
        try {
            ApiResponse<Void> response = messageService.seenMessages(userId, roomId).get();
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to fetch messages: " + e.getMessage(), null);
        }
    }

}
