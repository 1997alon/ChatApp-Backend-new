package com.example.ChatApp.service;

import com.example.ChatApp.dto.MessageDTO;
import com.example.ChatApp.dto.RoomDTO;
import com.example.ChatApp.model.*;
import com.example.ChatApp.repository.MessageRepository;
import com.example.ChatApp.repository.MessageStatusRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MessageEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;

    public MessageEventPublisher(SimpMessagingTemplate messagingTemplate,
                                 MessageRepository messageRepository,
                                 MessageStatusRepository messageStatusRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.messageStatusRepository = messageStatusRepository;
    }

    public void notifyMessagesUpdated(Room room, List<User> users) {
        List<Message> messages = messageRepository.findAllByRoom(room);

        for (User user : users) {
            // Fetch status for each message for the current user
            List<MessageStatus> statuses = messageStatusRepository.findByUserAndMessageIn(user, messages);

            Map<Integer, Boolean> readMap = statuses.stream()
                    .collect(Collectors.toMap(
                            status -> status.getMessage().getId(),
                            MessageStatus::getIsRead
                    ));

            List<MessageDTO> messageDTOs = messages.stream()
                    .map(msg -> new MessageDTO(msg, readMap.getOrDefault(msg.getId(), false)))
                    .collect(Collectors.toList());

            String destination = "/topic/user/" + user.getId() + "/room/" + room.getId() + "/messages";
            messagingTemplate.convertAndSend(destination, messageDTOs);
        }
    }

}
