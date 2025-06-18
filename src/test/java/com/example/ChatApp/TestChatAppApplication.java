package com.example.ChatApp;

import org.springframework.boot.SpringApplication;

public class TestChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChatAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
