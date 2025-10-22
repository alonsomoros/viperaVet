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
        return dogApiClient.getAllBreeds();
    }

    @Transactional
    public List<BreedOutDTO> saveAllDogsBreeds() {
        log.info("Iniciando sincronización de razas de perro desde Dog API");

        // Buscar especie 'Perro' en la BBDD
        Specie dogSpecie = specieRepository.findByName("Perro")
                .orElseThrow(() -> new IllegalStateException("Especie 'Perro' no encontrada en la BBDD."));

        // Coger las razas de la API externa
        List<DogApiBreedInDTO> dogBreeds = getAllDogBreeds();
        if (dogBreeds == null || dogBreeds.isEmpty()) {
            log.info("No se encontraron razas de perro en la API externa.");
            return List.of();
        }

        // Filtrar las razas que ya existen en la BBDD
        List<Breed> existingBreeds = breedRepositoryAdapter.findBreedsBySpecieId(dogSpecie.getId());

        // Y obtener los IDs externos de las razas existentes
        Set<String> existingBreedsExternalIds = existingBreeds.stream()
                .map(Breed::getExternalApiId)
                .collect(Collectors.toSet());

        // Para filtrar las razas nuevas que no estén en nuestra BBDD
        List<DogApiBreedInDTO> newApiBreedsFiltered = dogBreeds.stream()
                .filter(dto -> !existingBreedsExternalIds.contains(dto.getId().toString()))
                .toList();

        if (newApiBreedsFiltered.isEmpty()) {
            log.info("No hay razas de perro nuevas para guardar. La BBDD está actualizada.");
            return List.of();
        }

        // Mapear DTO -> Entity
        List<Breed> breedsToSave = breedMapper.fromDogApiToEntity(newApiBreedsFiltered, dogSpecie);

        // Las vuelve a insertar todas (MAL!)
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
        log.error("Fallo al obtener razas de perro desde Dog API: {}", throwable.toString());
        return List.of();
    }
}
