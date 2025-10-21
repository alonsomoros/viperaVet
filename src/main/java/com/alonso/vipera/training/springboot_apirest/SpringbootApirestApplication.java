package com.alonso.vipera.training.springboot_apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "ApiTrainingApplication", description = "This API allows to create users for a Vet service where you can register your pets."))
public class SpringbootApirestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestApplication.class, args);
	}

}
