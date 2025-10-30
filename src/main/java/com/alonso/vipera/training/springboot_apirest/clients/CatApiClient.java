package com.alonso.vipera.training.springboot_apirest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.alonso.vipera.training.springboot_apirest.model.catBreedAPI.dto.in.CatApiBreedInDTO;

import java.util.List;

@FeignClient(name = "cat-api", url = "${thecatapi.url}")
public interface CatApiClient {

    @GetMapping("/v1/breeds")
    List<CatApiBreedInDTO> getAllBreeds();
}
