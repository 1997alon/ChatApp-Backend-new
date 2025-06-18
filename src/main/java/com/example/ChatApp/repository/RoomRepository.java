package com.example.ChatApp.repository;

import com.example.ChatApp.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query(value = """
    SELECT r.* FROM room r
    JOIN member m ON r.id = m.room_id
    LEFT JOIN (
        SELECT room_id, MAX(time) AS last_message_time
        FROM message
        GROUP BY room_id
    ) msg ON r.id = msg.room_id
    WHERE m.user_id = :userId
    ORDER BY COALESCE(msg.last_message_time, r.time) DESC
    """, nativeQuery = true)
    List<Room> findRoomsByUserSortedByLastMessageOrCreation(@Param("userId") Integer userId);

}
