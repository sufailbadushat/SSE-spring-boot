package com.event.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SseEventApplication {

	public static void main(String[] args) {
		SpringApplication.run(SseEventApplication.class, args);
	}

}
