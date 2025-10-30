package com.alonso.vipera.training.springboot_apirest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in.DogApiBreedInDTO;

import java.util.List;

@FeignClient(name = "dog-api", url = "${thedogapi.url}")
public interface DogApiClient {

    @GetMapping("/v1/breeds")
    List<DogApiBreedInDTO> getAllBreeds();
}
