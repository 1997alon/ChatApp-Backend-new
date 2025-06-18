package com.example.ChatApp.repository;

import com.example.ChatApp.model.Friend;
import com.example.ChatApp.model.FriendId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friend f " +
            "WHERE (f.friendOne.id = :id1 AND f.friendTwo.id = :id2) " +
            "   OR (f.friendOne.id = :id2 AND f.friendTwo.id = :id1)")
    boolean areUsersAlreadyFriends(@Param("id1") Integer id1, @Param("id2") Integer id2);

    // new method to get usernames of friends for user with id=:id
    @Query("SELECT CASE WHEN f.friendOne.id = :id THEN f.friendTwo.username ELSE f.friendOne.username END " +
            "FROM Friend f " +
            "WHERE f.friendOne.id = :id OR f.friendTwo.id = :id")
    List<String> getFriends(@Param("id") Integer id);
}
