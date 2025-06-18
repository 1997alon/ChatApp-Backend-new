package com.example.ChatApp.repository;

import com.example.ChatApp.dto.UnreadCountPerRoom;
import com.example.ChatApp.model.Message;
import com.example.ChatApp.model.MessageStatus;
import com.example.ChatApp.model.MessageStatusId;
import com.example.ChatApp.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageStatusRepository extends JpaRepository<MessageStatus, MessageStatusId> {
    List<MessageStatus> findByUserAndMessageIn(User user, List<Message> messages);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE message_status ms
        JOIN message m ON ms.message_id = m.id
        SET ms.is_read = true
        WHERE ms.user_id = :userId AND m.room_id = :roomId
        """, nativeQuery = true)
    void markAllMessagesAsReadByUserAndRoom(@Param("userId") int userId, @Param("roomId") int roomId);

    @Query(value = """
    SELECT
      m.user_id AS userId,
      m.room_id AS roomId,
      COALESCE(SUM(CASE WHEN ms.is_read = 0 THEN 1 ELSE 0 END), 0) AS unreadCount
    FROM
      member m
    LEFT JOIN
      message msg ON msg.room_id = m.room_id
    LEFT JOIN
      message_status ms ON ms.message_id = msg.id AND ms.user_id = m.user_id
    WHERE
      m.user_id = :userId
    GROUP BY
      m.user_id,
      m.room_id
    """, nativeQuery = true)
    List<UnreadCountPerRoom> countUnreadMessagesByUser(@Param("userId") int userId);


}