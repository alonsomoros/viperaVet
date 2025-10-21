package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.clients.DogApiClient;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.DogApiBreedDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogApiBreedsService {

    private final DogApiClient dogApiClient;
    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final SpecieRepositoryAdapter specieRepository;
    private final BreedMapper breedMapper;

    @CircuitBreaker(name = "dog-api-breeds", fallbackMethod = "getBreedsFallback")
    public List<DogApiBreedDTO> getAllDogBreeds() {
        return dogApiClient.getAllBreeds();
    }

    @Transactional
    public List<BreedOutDTO> saveAllDogsBreeds() {
        log.info("Iniciando sincronización de razas desde Dog API");

        // Buscar especie 'Perro' en la BBDD
        Specie dogSpecie = specieRepository.findByName("Perro")
                .orElseThrow(() -> new IllegalStateException("Especie 'Perro' no encontrada en la BBDD."));

        // Obtener todas las razas desde la API externa
        List<DogApiBreedDTO> dogBreeds = getAllDogBreeds();

        // Mapear DTO -> Entity
        List<Breed> breedsToSave = breedMapper.fromApiToEntity(dogBreeds, dogSpecie);
        
        // Las vuelve a insertar todas (MAL!)
        List<BreedOutDTO> savedBreeds = breedRepositoryAdapter.saveAllBreeds(breedsToSave)
                .stream()
                .map(breedMapper::toDTO)
                .toList();

        log.info("Sincronización completada: {} razas guardadas/actualizadas", savedBreeds.size());
        return savedBreeds;
    }

    /**
     * Este es el método Fallback. Se ejecutará si la llamada a la API falla.
     * Debe tener la misma firma que el método original, más un parámetro Throwable
     * (opcional).
     */
    public List<DogApiBreedDTO> getBreedsFallback(Throwable throwable) {
        log.error("Fallo al obtener razas desde Dog API: {}", throwable.toString());
        return List.of();
    }
}
