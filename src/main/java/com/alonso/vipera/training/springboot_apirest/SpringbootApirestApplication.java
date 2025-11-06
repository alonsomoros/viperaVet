package com.alonso.vipera.training.springboot_apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Clase principal de la aplicación Spring Boot.
 * Configura y arranca la aplicación con varias funcionalidades habilitadas,
 * como Feign Clients, Scheduling, JPA Auditing y Caching.
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableJpaAuditing
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "ApiTrainingApplication", description = "This API allows to create users for a Vet service where you can register your pets."))
public class SpringbootApirestApplication {

	/**
	 * Método principal que arranca la aplicación Spring Boot.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestApplication.class, args);
	}

}
