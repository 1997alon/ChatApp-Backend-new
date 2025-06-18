package com.example.ChatApp.service;
import com.example.ChatApp.dto.*;
import com.example.ChatApp.model.*;
import com.example.ChatApp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendRequestEventPublisher friendRequestEventPublisher;

    @Autowired
    private Executor executor;

    public CompletableFuture<ApiResponse<Void>> addFriendRequest(AddFriendRequest friendName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findByName(friendName.getName()).orElse(null);
                if (user == null) {
                    return new ApiResponse<>(false, "There's no name for the request", null);
                }

                User friendUser = userRepository.findByName(friendName.getFriendName()).orElse(null);
                if (friendUser == null) {
                    return new ApiResponse<>(false, "There's no name for the friend", null);
                }

                logger.info("Sender ID: {}, Receiver ID: {}", user.getId(), friendUser.getId());

                int lowerId = Math.min(user.getId(), friendUser.getId());
                int higherId = Math.max(user.getId(), friendUser.getId());

                if (friendRepository.areUsersAlreadyFriends(lowerId, higherId)) {
                    return new ApiResponse<>(false, "You are already friends", null);
                }

                if (requestRepository.pendingRequestExists(user.getId(), friendUser.getId())) {
                    return new ApiResponse<>(false, "Friend request already sent and pending", null);
                }

                Request friendRequest = new Request(user, friendUser, RequestStatus.PENDING);
                logger.info("FriendRequest: {}", friendRequest);
                requestRepository.save(friendRequest);

                friendRequestEventPublisher.notifyReceiver(friendRequest);

                return new ApiResponse<>(true, "Friend request sent", null);
            } catch (Exception e) {
                logger.error("Error fetching friend name " + friendName, e);
                return new ApiResponse<>(false, "Failed to add friend request: " + e.getMessage(), null);
            }
        }, executor);
    }

    public CompletableFuture<ApiResponse<List<RequestDTO>>> pendingRequests(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findByName(name).orElse(null);
                if (user == null) {
                    return new ApiResponse<>(false, "There's no name for the request", null);
                }

                List<Request> pendingRequests = requestRepository.findPendingRequestsByReceiverId(user.getId());

                List<RequestDTO> requestDTOs = pendingRequests.stream()
                        .map(RequestDTO::new)
                        .collect(Collectors.toList());

                return new ApiResponse<>(true, "Fetched pending requests", requestDTOs);

            } catch (Exception e) {
                logger.error("Error fetching pending requests ", e);
                return new ApiResponse<>(false, "Error fetching pending requests: " + e.getMessage(), null);
            }
        }, executor);
    }
}

