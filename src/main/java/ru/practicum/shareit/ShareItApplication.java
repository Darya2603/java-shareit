package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShareItApplication.class, args);
		System.out.println("Приложение запущено на http://localhost:8080");
	}
}