package com.example.ChatApp.service;

import com.example.ChatApp.dto.ReplyDTO;
import com.example.ChatApp.dto.RequestDTO;
import com.example.ChatApp.model.Reply;
import com.example.ChatApp.model.Request;
import com.example.ChatApp.repository.RequestRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final RequestRepository requestRepository;

    public FriendRequestEventPublisher(SimpMessagingTemplate messagingTemplate,
                                       RequestRepository requestRepository) {
        this.messagingTemplate = messagingTemplate;
        this.requestRepository = requestRepository;
    }

    public void notifyReceiver(Request request) {
        // Convert the request to a DTO (optional but cleaner)
        RequestDTO requestDTO = new RequestDTO(request);

        // Send the request notification to the receiver via WebSocket
        String destination = "/topic/user/" + request.getReceiver().getId() + "/friend-requests";
        messagingTemplate.convertAndSend(destination, requestDTO);
    }

    public void notifyReply(Reply reply) {
        // You should create a ReplyDTO class similar to RequestDTO for clean separation
        ReplyDTO replyDTO = new ReplyDTO(reply);

        String destination = "/topic/user/" + reply.getReceiver().getId() + "/friend-replies";
        messagingTemplate.convertAndSend(destination, replyDTO);
    }
}
