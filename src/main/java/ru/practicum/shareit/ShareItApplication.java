package ru.practicum.shareit;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApplication {
	private static final int PORT = 8080;
	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		SpringApplication.run(ShareItApplication.class, args);
	}
}