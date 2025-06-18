package com.example.ChatApp.repository;

import com.example.ChatApp.model.Message;
import com.example.ChatApp.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByRoom(Room room);

    // New method to get messages for a room ordered by time ascending
    List<Message> findAllByRoomOrderByTimeAsc(Room room);
}
