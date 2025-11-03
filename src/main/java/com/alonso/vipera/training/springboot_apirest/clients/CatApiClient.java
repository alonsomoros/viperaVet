package com.alonso.vipera.training.springboot_apirest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.alonso.vipera.training.springboot_apirest.model.cat_breed_api.dto.in.CatApiBreedInDTO;

import java.util.List;

/**
 * Cliente Feign para consumir The Cat API.
 */
@FeignClient(name = "cat-api", url = "${thecatapi.url}")
public interface CatApiClient {

    /**
     * Obtiene la lista completa de razas de gatos desde The Cat API.
     * 
     * @return Lista de razas de gatos.
     */
    @GetMapping
    List<CatApiBreedInDTO> getAllBreeds();
}
