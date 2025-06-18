package com.example.ChatApp.repository;

import com.example.ChatApp.model.Member;
import com.example.ChatApp.model.MemberId;
import com.example.ChatApp.model.Room;
import com.example.ChatApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, MemberId> {
    // saveAll() is inherited from JpaRepository, no need to declare explicitly
    List<Member> findAllByUserId(Integer userId);

    Boolean existsByRoomAndUser(Room room, User sender);

    List<Member> findAllByRoom(Room room);
}
