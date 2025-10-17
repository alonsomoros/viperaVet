package com.alonso.vipera.training.springboot_apirest.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.clients.DogApiClient;
import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.DogBreedDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class DogBreedService {

    private final DogApiClient dogApiClient;

    public DogBreedService(DogApiClient dogApiClient) {
        this.dogApiClient = dogApiClient;
    }

    @CircuitBreaker(name = "dog-api-breeds", fallbackMethod = "getBreedsFallback")
    public List<DogBreedDTO> getAllDogBreeds() {
        return dogApiClient.getAllBreeds();
    }

    /**
     * Este es el método Fallback. Se ejecutará si la llamada a la API falla.
     * Debe tener la misma firma que el método original, más un parámetro Throwable
     * (opcional).
     */
    public List<DogBreedDTO> getBreedsFallback(Throwable throwable) {
        System.err.println("Error al llamar a la API de razas: " + throwable.getMessage());

        List<DogBreedDTO> fallbackEmptyList = new ArrayList<>();
        return fallbackEmptyList;
    }
}
