package com.example.ChatApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    @Primary
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(10);  // adjust pool size as needed
    }
}