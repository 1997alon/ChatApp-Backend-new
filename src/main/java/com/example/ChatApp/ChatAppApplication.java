package com.example.ChatApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ChatAppApplication {
	private static final Logger logger = LoggerFactory.getLogger(ChatAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
		logger.info("Spring Boot application started! Server is running...");
	}

}
