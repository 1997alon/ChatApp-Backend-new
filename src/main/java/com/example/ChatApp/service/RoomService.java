package com.example.ChatApp.service;

import com.example.ChatApp.dto.NewRoomRequest;
import com.example.ChatApp.dto.ApiResponse;
import com.example.ChatApp.dto.RoomDTO;
import com.example.ChatApp.dto.UnreadCountPerRoom;
import com.example.ChatApp.model.Member;
import com.example.ChatApp.model.Room;
import com.example.ChatApp.model.User;
import com.example.ChatApp.repository.MemberRepository;
import com.example.ChatApp.repository.MessageStatusRepository;
import com.example.ChatApp.repository.RoomRepository;
import com.example.ChatApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final RoomEventPublisher roomEventPublisher;

    public RoomService(RoomRepository roomRepository,
                       UserRepository userRepository,
                       MemberRepository memberRepository,
                       RoomEventPublisher roomEventPublisher,
                       MessageStatusRepository messageStatusRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.roomEventPublisher = roomEventPublisher;
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

        for (UnreadCountPerRoom u : unreadCounts) {
            logger.info("UserId: {}, RoomId: {}, UnreadCount: {}", u.getUserId(), u.getRoomId(), u.getUnreadCount());
        }

        List<RoomDTO> roomDTOs = sortedRooms.stream()
                .map(room -> new RoomDTO(
                        room,
                        unreadCountMap.getOrDefault(room.getId(), 0)
                ))
                .collect(Collectors.toList());

        return roomDTOs;
    }

    public void logAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            logger.info("Total users found: {}", users.size());

            for (User user : users) {
                logger.info("User - ID: {}, Name: {}", user.getId(), user.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to fetch all users", e);
        }
    }

    public CompletableFuture<ApiResponse<List<RoomDTO>>> newRoom(NewRoomRequest request) {
        logger.info("Received newRoom request: name='{}', isGroup={}, createdBy='{}', names={}",
                request.getName(), request.getIsGroup(), request.getCreatedBy(), request.getNames());
        logAllUsers();

        return CompletableFuture.supplyAsync(() -> {
            try {
                // Find creator user
                User creator = userRepository.findByName(request.getCreatedBy()).orElse(null);
                if (creator == null) {
                    logger.warn("Creator user '{}' not found", request.getCreatedBy());
                    return new ApiResponse<>(false, "Creator user not found");
                }
                logger.debug("Creator user found: id={}, name={}", creator.getId(), creator.getName());

                List<String> requestedNames = request.getNames();
                List<User> users = new ArrayList<>(userRepository.findAllByNameIn(requestedNames));
                logger.debug("Users fetched from repository: {}", users.stream().map(User::getName).toList());

                // Check for missing users explicitly
                Set<String> foundNames = users.stream()
                        .map(User::getName)
                        .collect(Collectors.toSet());

                List<String> missingNames = requestedNames.stream()
                        .filter(name -> !foundNames.contains(name))
                        .collect(Collectors.toList());

                if (!missingNames.isEmpty()) {
                    logger.warn("The following requested users were not found: {}", missingNames);
                    return new ApiResponse<>(false, "Users not found: " + String.join(", ", missingNames));
                }

                // Create the new room
                Room createdRoom = roomRepository.save(
                        new Room(request.getName(), request.getIsGroup(), LocalDateTime.now(), creator)

                );
                logger.info("Room '{}' created with id {}", createdRoom.getName(), createdRoom.getId());

                // Ensure creator is included in the members list
                if (users.stream().noneMatch(u -> u.getId().equals(creator.getId()))) {
                    logger.debug("Creator user not in users list, adding creator");
                    users.add(creator);
                }

                // Create and save members
                List<Member> members = users.stream()
                        .map(user -> new Member(createdRoom, user))
                        .collect(Collectors.toList());
                memberRepository.saveAll(members);
                logger.info("Saved {} members for room id {}", members.size(), createdRoom.getId());

                // Notify about the new room creation
                logger.debug("Before notifyRoomCreated, users size: {}", users.size());
                roomEventPublisher.notifyRoomCreated(createdRoom, users);
                logger.debug("notifyRoomCreated called");

                List<Room> sortedRooms = roomRepository.findRoomsByUserSortedByLastMessageOrCreation(creator.getId());
                List<RoomDTO> roomDTOs = getUnreadMessageList(sortedRooms, creator.getId());

                logger.info("Returning {} rooms for creator id {}", roomDTOs.size(), creator.getId());

                return new ApiResponse<>(true, "Room created successfully", roomDTOs);

            } catch (Exception ex) {
                logger.error("Failed to create room due to exception", ex);
                return new ApiResponse<>(false, "Failed to create room: " + ex.getMessage());
            }
        }, executor);
    }


    public CompletableFuture<ApiResponse<List<RoomDTO>>> getRoomsForUser(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Check if user exists
                User user = userRepository.findById(id).orElse(null);
                if (user == null) {
                    logger.warn("User with ID {} not found", id);
                    return new ApiResponse<>(false, "User not found", null);
                }

                // Get all Member entries for this user
                List<Member> userMemberships = memberRepository.findAllByUserId(id);
                logger.info("User {} is member of {} rooms", id, userMemberships.size());

                List<Room> sortedRooms = roomRepository.findRoomsByUserSortedByLastMessageOrCreation(id);


                List<RoomDTO> roomDTOs = getUnreadMessageList(sortedRooms, id);

                return new ApiResponse<>(true, "Rooms fetched successfully", roomDTOs);
            } catch (Exception e) {
                logger.error("Error fetching rooms for user " + id, e);
                return new ApiResponse<>(false, "Failed to get rooms: " + e.getMessage(), null);
            }
        }, executor);
    }

}
