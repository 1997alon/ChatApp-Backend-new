package com.example.ChatApp.service;

import com.example.ChatApp.dto.RoomDTO;
import com.example.ChatApp.dto.UnreadCountPerRoom;
import com.example.ChatApp.model.Room;
import com.example.ChatApp.model.User;
import com.example.ChatApp.repository.MemberRepository;
import com.example.ChatApp.repository.MessageStatusRepository;
import com.example.ChatApp.repository.RoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoomEventPublisher {

    // For example, inject your WebSocket handler or messaging template here
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MessageStatusRepository messageStatusRepository;

    public RoomEventPublisher(SimpMessagingTemplate messagingTemplate, MemberRepository memberRepository, RoomRepository roomRepository, MessageStatusRepository messageStatusRepository) {
        this.messagingTemplate = messagingTemplate;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageStatusRepository = messageStatusRepository;
    }

    public List<RoomDTO> getUnreadMessageList(List<Room> sortedRooms, Integer userId) {
        List<Integer> roomIds = sortedRooms.stream()
                .map(Room::getId)
                .collect(Collectors.toList());

        List<UnreadCountPerRoom> unreadCounts = messageStatusRepository
                .countUnreadMessagesByUser(userId);

        Map<Integer, Integer> unreadCountMap = unreadCounts.stream()
                .collect(Collectors.toMap(
                        UnreadCountPerRoom::getRoomId,
                        UnreadCountPerRoom::getUnreadCount
                ));

        List<RoomDTO> roomDTOs = sortedRooms.stream()
                .map(room -> new RoomDTO(
                        room,
                        unreadCountMap.getOrDefault(room.getId(), 0)
                ))
                .collect(Collectors.toList());

        return roomDTOs;
    }


    public void notifyRoomCreated(Room room, List<User> users) {
        for (User user : users) {
            List<Room> sortedRooms = roomRepository.findRoomsByUserSortedByLastMessageOrCreation(user.getId());

            // 2. Map to DTOs
            List<RoomDTO> roomDTOs = getUnreadMessageList(sortedRooms, user.getId());

            // 3. Send full updated room list to this user
            String destination = "/topic/user/" + user.getId() + "/rooms";
            messagingTemplate.convertAndSend(destination, roomDTOs);
        }
    }
}