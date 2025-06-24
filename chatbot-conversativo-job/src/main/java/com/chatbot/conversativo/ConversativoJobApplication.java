package com.chatbot.conversativo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConversativoJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversativoJobApplication.class, args);
	}

}
