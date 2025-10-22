package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.clients.CatApiClient;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.catBreedAPI.dto.in.CatApiBreedInDTO;
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
public class CatApiBreedsService {

    private final CatApiClient catApiClient;
    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final SpecieRepositoryAdapter specieRepository;
    private final BreedMapper breedMapper;

    @CircuitBreaker(name = "cat-api-breeds", fallbackMethod = "getBreedsFallback")
    public List<CatApiBreedInDTO> getAllCatBreeds() {
        return catApiClient.getAllBreeds();
    }

    @Transactional
    public List<BreedOutDTO> saveAllCatsBreeds() {
        log.info("Iniciando sincronización de razas de gato desde Cat API");

        // Buscar especie 'Gato' en la BBDD
        Specie catSpecie = specieRepository.findByName("Gato")
                .orElseThrow(() -> new IllegalStateException("Especie 'Gato' no encontrada en la BBDD."));

        // Coger las razas de la API externa
        List<CatApiBreedInDTO> catBreeds = getAllCatBreeds();
        if (catBreeds == null || catBreeds.isEmpty()) {
            log.info("No se encontraron razas de gato en la API externa.");
            return List.of();
        }

        // Filtrar las razas que ya existen en la BBDD
        List<Breed> existingBreeds = breedRepositoryAdapter.findBreedsBySpecieId(catSpecie.getId());

        // Y obtener los IDs externos de las razas existentes
        Set<String> existingBreedsExternalIds = existingBreeds.stream()
                .map(Breed::getExternalApiId)
                .collect(Collectors.toSet());

        // Para filtrar las razas nuevas que no estén en nuestra BBDD
        List<CatApiBreedInDTO> newApiBreedsFiltered = catBreeds.stream()
                .filter(dto -> !existingBreedsExternalIds.contains(dto.getId()))
                .toList();

        if (newApiBreedsFiltered.isEmpty()) {
            log.info("No hay razas de gato nuevas para guardar. La BBDD está actualizada.");
            return List.of();
        }

        // Mapear DTO -> Entity
        List<Breed> breedsToSave = breedMapper.fromCatApiToEntity(newApiBreedsFiltered, catSpecie);

        // Las vuelve a insertar todas (MAL!)
        List<BreedOutDTO> savedBreeds = breedRepositoryAdapter.saveAllBreeds(breedsToSave)
                .stream()
                .map(breedMapper::toDTO)
                .toList();

        log.info("Sincronización completada: {} razas de gato guardadas/actualizadas", savedBreeds.size());
        return savedBreeds;
    }

    /**
     * Este es el método Fallback. Se ejecutará si la llamada a la API falla.
     * Debe tener la misma firma que el método original, más un parámetro Throwable
     * (opcional).
     */
    public List<CatApiBreedInDTO> getBreedsFallback(Throwable throwable) {
        log.error("Fallo al obtener razas de gato desde Cat API: {}", throwable.toString());
        return List.of();
    }
}
