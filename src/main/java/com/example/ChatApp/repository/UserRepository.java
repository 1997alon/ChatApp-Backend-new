package com.example.ChatApp.repository;
import com.example.ChatApp.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmailOrName(String username, String email, String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findById(Integer id);

    List<User> findAllByNameIn(List<String> names);

    @Transactional
    void deleteByUsername(String username);

    @Query("SELECT u.name FROM User u WHERE u.name <> :name")
    List<String> findAllNamesExcept(@Param("name") String name);
}
