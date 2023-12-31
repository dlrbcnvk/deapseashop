package com.example.deapseashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class DeapseashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeapseashopApplication.class, args);
	}
}
