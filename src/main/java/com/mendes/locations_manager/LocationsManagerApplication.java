package com.mendes.locations_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LocationsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationsManagerApplication.class, args);
	}

}
