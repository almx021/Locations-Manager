package com.mendes.desafio_tecnico_backend_nuven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DesafioTecnicoBackendNuvenApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioTecnicoBackendNuvenApplication.class, args);
	}

}
