package com.example.ChatApp.service;

import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.ReplyDTO;
import com.example.ChatApp.dto.ReplyFriendRequest;
import com.example.ChatApp.model.*;
import com.example.ChatApp.repository.FriendRepository;
import com.example.ChatApp.repository.ReplyRepository;
import com.example.ChatApp.repository.RequestRepository;
import com.example.ChatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class ReplyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private FriendRequestEventPublisher friendRequestEventPublisher;

    @Autowired
    private Executor executor;

    public CompletableFuture<ApiResponse<Void>> replyToFriendRequest(ReplyFriendRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User sender = userRepository.findByName(request.getSenderName()).orElse(null);
                User receiver = userRepository.findByName(request.getReceiverName()).orElse(null);

                if (sender == null || receiver == null) {
                    return new ApiResponse<>(false, "Invalid sender or receiver", null);
                }

                Optional<Request> optionalRequest = requestRepository
                        .findRequestBySenderIdAndReceiverIdAndStatus(
                                receiver.getId(), sender.getId(), RequestStatus.PENDING
                        );

                if (optionalRequest.isEmpty()) {
                    return new ApiResponse<>(false, "No pending friend request found", null);
                }

                Request friendRequest = optionalRequest.get();

                // Update request status
                if (request.isApproved()) {
                    friendRequest.setStatus(RequestStatus.ACCEPTED);

                    // Ensure ordering: friendOne < friendTwo
                    User friendOne = sender.getId() < receiver.getId() ? sender : receiver;
                    User friendTwo = sender.getId() > receiver.getId() ? sender : receiver;

                    // Prevent duplicate friendships
                    if (!friendRepository.areUsersAlreadyFriends(friendOne.getId(), friendTwo.getId())) {
                        Friend friend = new Friend(friendOne, friendTwo);
                        friendRepository.save(friend);
                    }

                } else {
                    friendRequest.setStatus(RequestStatus.REJECTED);
                }

                // Save changes
                requestRepository.save(friendRequest);

                Reply reply = new Reply(sender, receiver, request.isApproved());
                replyRepository.save(reply);
                friendRequestEventPublisher.notifyReply(reply);

                return new ApiResponse<>(true, "Reply submitted", null);
            } catch (Exception e) {
                return new ApiResponse<>(false, "Failed to reply: " + e.getMessage(), null);
            }
        }, executor);
    }

    public CompletableFuture<ApiResponse<List<ReplyDTO>>> getReplies(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findByName(name).orElse(null);

                if(user == null) {
                    return new ApiResponse<>(false, "user is not exist");
                }

                List<Reply> replies = replyRepository.findByReceiver(user);
                List<ReplyDTO> replyDTOs = replies.stream()
                        .map(ReplyDTO::new)
                        .toList();

                return new ApiResponse<>(true, "Replies fetched successfully", replyDTOs);

            } catch (Exception e) {
                return new ApiResponse<>(false, "Failed to reply: " + e.getMessage(), null);
            }
        }, executor);
    }


}
