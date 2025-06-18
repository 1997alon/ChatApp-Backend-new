package com.example.ChatApp.service;

import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.MessageDTO;
import com.example.ChatApp.dto.MessageSentDTO;
import com.example.ChatApp.model.*;
import com.example.ChatApp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository; // If you track message read/unread status
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MessageEventPublisher messageEventPublisher; // To notify members
    private final RoomEventPublisher roomEventPublisher;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public MessageService(MessageRepository messageRepository,
                          MessageStatusRepository messageStatusRepository,
                          UserRepository userRepository,
                          RoomRepository roomRepository,
                          MemberRepository memberRepository,
                          MessageEventPublisher messageEventPublisher,
                          RoomEventPublisher roomEventPublisher) {
        this.messageRepository = messageRepository;
        this.messageStatusRepository = messageStatusRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.messageEventPublisher = messageEventPublisher;
        this.roomEventPublisher = roomEventPublisher;
    }

    public CompletableFuture<ApiResponse<List<MessageDTO>>> sendNewMessage(MessageSentDTO newMsg) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Room room = roomRepository.findById(newMsg.getRoomId()).orElse(null);
                if (room == null) {
                    return new ApiResponse<>(false, "Room not found", null);
                }

                User sender = userRepository.findById(newMsg.getSenderId()).orElse(null);
                if (sender == null) {
                    return new ApiResponse<>(false, "Sender user not found", null);
                }

                // 3. Check if user is member of the room
                boolean isMember = memberRepository.existsByRoomAndUser(room, sender);
                if (!isMember) {
                    return new ApiResponse<>(false, "User is not a member of the room", null);
                }

                MessageType messageType;
                try {
                    messageType = MessageType.valueOf(newMsg.getType());
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid message type '{}'", newMsg.getType());
                    return new ApiResponse<>(false, "Invalid message type", null);
                }

                Message message = new Message();
                message.setRoom(room);
                message.setSender(sender);
                message.setContent(newMsg.getContent());
                message.setType(messageType);
                message.setTime(LocalDateTime.now());

                Message savedMessage = messageRepository.save(message);
                logger.info("Message saved with id {}", savedMessage.getId());
                // 6. Notify all members about the new message
                List<User> usersInRoom = memberRepository.findAllByRoom(room).stream()
                        .map(Member::getUser)
                        .collect(Collectors.toList());
                messageEventPublisher.notifyMessagesUpdated(room, usersInRoom);
                roomEventPublisher.notifyRoomCreated(room,usersInRoom);
                for (User user : usersInRoom) {
                    boolean isRead = user.getId().equals(sender.getId()); // sender has read their own message
                    MessageStatus status = new MessageStatus(savedMessage, user, room, isRead);
                    messageStatusRepository.save(status); // make sure you have this repository
                }

                List<Message> messages = messageRepository.findAllByRoomOrderByTimeAsc(room);

                List<MessageDTO> messageDTOs = messages.stream()
                        .map(msg -> new MessageDTO(msg, true))
                        .collect(Collectors.toList());

                return new ApiResponse<>(true, "Message sent successfully", messageDTOs);

            } catch (Exception e) {
                logger.error("Failed to send message", e);
                return new ApiResponse<>(false, "Failed to send message: " + e.getMessage(), null);
            }
        }, executor);
    }

    public CompletableFuture<ApiResponse<List<MessageDTO>>> getMessages(Integer userId, Integer roomId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findById(userId).orElse(null);
                if(user == null) {
                    return new ApiResponse<>(false, "user does not exist");
                }
                Room room = roomRepository.findById(roomId).orElse(null);
                if(room == null) {
                    return new ApiResponse<>(false, "room does not exist");
                }
                boolean isMember = memberRepository.existsByRoomAndUser(room, user);
                if (!isMember) {
                    return new ApiResponse<>(false, "User is not a member of the room", null);
                }

                List<Message> messages = messageRepository.findAllByRoomOrderByTimeAsc(room);

                List<MessageDTO> messageDTOs = messages.stream()
                        .map(msg -> new MessageDTO(msg, true))
                        .collect(Collectors.toList());

                messageStatusRepository.markAllMessagesAsReadByUserAndRoom(userId, roomId);

                return new ApiResponse<>(true, "Message sent successfully", messageDTOs);

            } catch (Exception e) {
                return new ApiResponse<>(false, "Failed to get messages" + e.getMessage(), null);
            }
        }, executor);
    }

    public CompletableFuture<ApiResponse<Void>> seenMessages(Integer userId, Integer roomId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userRepository.findById(userId).orElse(null);
                if(user == null) {
                    return new ApiResponse<>(false, "user does not exist");
                }
                Room room = roomRepository.findById(roomId).orElse(null);
                if(room == null) {
                    return new ApiResponse<>(false, "room does not exist");
                }
                boolean isMember = memberRepository.existsByRoomAndUser(room, user);
                if (!isMember) {
                    return new ApiResponse<>(false, "User is not a member of the room", null);
                }
                messageStatusRepository.markAllMessagesAsReadByUserAndRoom(userId, roomId);
                return new ApiResponse<>(true, "'successfully seen all the messages", null);
            } catch (Exception e) {
                return new ApiResponse<>(false, "Failed to seen messages" + e.getMessage(), null);
            }
        }, executor);
    }

}
