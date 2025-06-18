package com.example.ChatApp.repository;

import com.example.ChatApp.model.Reply;
import com.example.ChatApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByReceiver(User receiver);
}
