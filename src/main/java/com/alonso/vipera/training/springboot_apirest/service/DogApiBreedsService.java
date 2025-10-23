package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.clients.DogApiClient;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.in.DogApiBreedInDTO;
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
    public List<DogApiBreedInDTO> getAllDogBreeds() {
        log.debug("Contactando a Dog API para obtener razas...");
        return dogApiClient.getAllBreeds();
    }

    @Transactional
    public List<BreedOutDTO> saveAllDogsBreeds() {
        log.info("Iniciando sincronización de razas de perro desde Dog API");

        log.debug("Buscando especie 'Perro' en la BBDD...");
        Specie dogSpecie = specieRepository.findByName("Perro")
                .orElseThrow(() -> {
                    log.error("La especie 'Perro' no fue encontrada en la BBDD. No se puede sincronizar.");
                    return new IllegalStateException("Especie 'Perro' no encontrada en la BBDD.");
                });
        log.debug("Especie 'Perro' encontrada con ID: {}", dogSpecie.getId());

        List<DogApiBreedInDTO> dogBreeds = getAllDogBreeds();
        if (dogBreeds == null || dogBreeds.isEmpty()) {
            log.info("No se encontraron razas de perro en la API externa.");
            return List.of();
        }
        log.debug("Obtenidas {} razas desde Dog API", dogBreeds.size());

        log.debug("Buscando razas de perro existentes en la BBDD para la especie ID: {}", dogSpecie.getId());
        List<Breed> existingBreeds = breedRepositoryAdapter.findBreedsBySpecieId(dogSpecie.getId());
        Set<String> existingBreedsExternalIds = existingBreeds.stream()
                .map(Breed::getExternalApiId)
                .collect(Collectors.toSet());
        log.debug("Razas existentes encontradas en la BBDD: {}", existingBreedsExternalIds);

        log.debug("Filtrando razas nuevas que no existen en la BBDD...");
        List<DogApiBreedInDTO> newApiBreedsFiltered = dogBreeds.stream()
                .filter(dto -> !existingBreedsExternalIds.contains(dto.getId().toString()))
                .toList();
        log.debug("Hay {} razas nuevas", newApiBreedsFiltered.size());

        if (newApiBreedsFiltered.isEmpty()) {
            log.info("No hay razas de perro nuevas para guardar. La BBDD está actualizada.");
            return List.of();
        }

        // Mapear DTO -> Entity
        List<Breed> breedsToSave = breedMapper.fromDogApiToEntity(newApiBreedsFiltered, dogSpecie);

        log.debug("Guardando/actualizando {} razas de perro en la BBDD...", breedsToSave.size());
        List<BreedOutDTO> savedBreeds = breedRepositoryAdapter.saveAllBreeds(breedsToSave)
                .stream()
                .map(breedMapper::toDTO)
                .toList();

        log.info("Sincronización completada: {} razas de perro guardadas/actualizadas", savedBreeds.size());
        return savedBreeds;
    }

    /**
     * Este es el método Fallback. Se ejecutará si la llamada a la API falla.
     * Debe tener la misma firma que el método original, más un parámetro Throwable
     * (opcional).
     */
    public List<DogApiBreedInDTO> getBreedsFallback(Throwable throwable) {
        log.error("Fallo al obtener razas de perro desde Dog API: {}", throwable);
        return List.of();
    }
}
