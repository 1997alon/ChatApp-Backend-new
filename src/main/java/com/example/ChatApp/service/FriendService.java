package com.example.ChatApp.service;

import com.example.ChatApp.dto.*;
import com.example.ChatApp.model.*;
import com.example.ChatApp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ReplyRepository replyRepository;
    private final FriendRequestEventPublisher friendRequestEventPublisher;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public FriendService(FriendRepository friendRepository, UserRepository userRepository, RequestRepository requestRepository, FriendRequestEventPublisher friendRequestEventPublisher, ReplyRepository replyRepository){
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.friendRequestEventPublisher = friendRequestEventPublisher;
        this.replyRepository = replyRepository;
    }



    public CompletableFuture<ApiResponse<List<String>>> getFriends(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findByName(name).orElse(null);
                if(user == null) {
                    return new ApiResponse<>(false, "Invalid user name", null);
                }

                List<String> friends = friendRepository.getFriends(user.getId());
                return new ApiResponse<>(true, "Friends retrieved successfully", friends);

            } catch (Exception e) {
                logger.error("Error processing get friends", e);
                return new ApiResponse<>(false, "Failed to get friends: " + e.getMessage(), null);
            }
        }, executor);
    }
}
