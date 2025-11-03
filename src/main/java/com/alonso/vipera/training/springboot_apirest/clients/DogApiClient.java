package com.alonso.vipera.training.springboot_apirest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in.DogApiBreedInDTO;

import java.util.List;

/**
 * Cliente Feign para consumir The Dog API.
 */
@FeignClient(name = "dog-api", url = "${thedogapi.url}")
public interface DogApiClient {

    /**
     * Obtiene la lista completa de razas de perros desde The Dog API.
     * 
     * @return Lista de razas de perros.
     */
    @GetMapping
    List<DogApiBreedInDTO> getAllBreeds();
}
