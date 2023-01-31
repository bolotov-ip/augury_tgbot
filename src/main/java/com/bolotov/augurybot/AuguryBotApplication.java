package com.bolotov.augurybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuguryBotApplication {

	public static void main(String[] args) {

		SpringApplication.run(AuguryBotApplication.class, args);
		System.out.println("Running");
	}

}
