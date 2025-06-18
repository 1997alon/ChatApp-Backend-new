package com.example.ChatApp.controller;

import com.example.ChatApp.dto.NewRoomRequest;
import com.example.ChatApp.model.User;
import com.example.ChatApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class RoomControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RoomControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void testCreateRoomSuccess() throws Exception {
        User user1 = new User("creatorUser", "pass1", "creator", "creator@example.com");
        User user2 = new User("userA", "pass2", "userA", "usera@example.com");
        User user3 = new User("userB", "pass3", "userB", "userb@example.com");
        userRepository.saveAll(List.of(user1, user2, user3));
        NewRoomRequest request = new NewRoomRequest(
                "Test Room",
                true,
                Arrays.asList("userA", "userB"), // <-- correct way to create List<String>
                "creator"
        );        request.setName("Test Room");

        String jsonRequest = objectMapper.writeValueAsString(request);
        logger.debug("testCreateRoomSuccess - Request JSON: {}", jsonRequest);

        String response = mockMvc.perform(post("/room/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[?(@.name=='Test Room')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        logger.debug("testCreateRoomSuccess - Response JSON: {}", response);
    }

    @Test
    void testCreateRoomWithInvalidCreator() throws Exception {
        User user1 = new User("d", "d", "d", "d@example.com");
        User user2 = new User("e", "e", "e", "e@example.com");
        User user3 = new User("f", "f", "f", "f@example.com");
        userRepository.saveAll(List.of(user1, user2, user3));
        NewRoomRequest request = new NewRoomRequest();
        request.setName("Test Room");
        request.setIsGroup(true);
        request.setCreatedBy("nonexistent"); // invalid 'name'
        request.setNames(Arrays.asList("e", "f"));

        String jsonRequest = objectMapper.writeValueAsString(request);
        logger.debug("testCreateRoomWithInvalidCreator - Request JSON: {}", jsonRequest);

        String response = mockMvc.perform(post("/room/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Creator user not found"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        request.setCreatedBy("d");
        request.setNames(Arrays.asList("e", "none"));
        String jsonRequest2 = objectMapper.writeValueAsString(request);
        String response2 = mockMvc.perform(post("/room/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Some names were not found"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        logger.debug("testCreateRoomWithInvalidCreator - Response JSON: {}", response);
    }

//    @Test
//    void testCreateRoomWithMissingUsers() throws Exception {
//        User user1 = new User("a", "a", "a", "a@example.com");
//        User user2 = new User("b", "b", "b", "b@example.com");
//        User user3 = new User("c", "c", "c", "c@example.com");
//        userRepository.saveAll(List.of(user1, user2, user3));
//        NewRoomRequest request = new NewRoomRequest();
//        request.setName("Test Room");
//        request.setIsGroup(true);
//        request.setCreatedBy("a");
//        request.setNames(Arrays.asList("b", "nonexistent")); // one invalid name
//
//        String jsonRequest = objectMapper.writeValueAsString(request);
//        logger.debug("testCreateRoomWithMissingUsers - Request JSON: {}", jsonRequest);
//
//        String response = mockMvc.perform(post("/room/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("Some names were not found"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        logger.debug("testCreateRoomWithMissingUsers - Response JSON: {}", response);
//    }
}
