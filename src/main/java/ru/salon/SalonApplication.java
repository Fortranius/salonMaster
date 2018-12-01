package ru.salon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SalonApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalonApplication.class, args);
	}
}
