package com.example.ChatApp.controller;

import com.example.ChatApp.Store.ActiveUserStore;
import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.NewRoomRequest;
import com.example.ChatApp.dto.RoomDTO;
import com.example.ChatApp.model.Room;
import com.example.ChatApp.model.User;
import com.example.ChatApp.service.RoomService;
import com.example.ChatApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private ActiveUserStore activeUserStore;

    @PostMapping("/new")
    public ApiResponse<List<RoomDTO>> newRoom(@Valid @RequestBody NewRoomRequest request) {
        try {
            // wait for async service to complete
            ApiResponse<List<RoomDTO>> response = roomService.newRoom(request).get();

            return response;

        } catch (Exception e) {
            // handle exceptions properly
            return new ApiResponse<>(false, "Failed to create room: " + e.getMessage(), null);
        }
    }

    @GetMapping("/get")
    public ApiResponse<List<RoomDTO>> getRooms(@RequestParam("id") Integer userId) {
        try {
            // Wait for the async service call to complete
            ApiResponse<List<RoomDTO>> response = roomService.getRoomsForUser(userId).get();
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to get rooms: " + e.getMessage(), null);
        }
    }
}
