package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.clients.DogApiClient;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in.DogApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio especializado para la sincronización de razas de perro desde la Dog API externa.
 * 
 * Este servicio se encarga de obtener datos de razas de perro desde una API externa,
 * compararlas con los datos existentes en la base de datos local, y sincronizar
 * únicamente las razas nuevas para evitar duplicados.
 * 
 * Implementa patrones de Circuit Breaker para tolerancia a fallos y manejo de caché
 * para optimizar el rendimiento de las consultas posteriores.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DogApiBreedsService {

    private final DogApiClient dogApiClient;
    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final SpecieRepositoryAdapter specieRepository;
    private final BreedMapper breedMapper;

    /**
     * Obtiene todas las razas de perro desde la Dog API externa.
     * 
     * Implementa el patrón Circuit Breaker para manejar fallos de la API externa.
     * En caso de fallo, se ejecutará el método fallback que retorna una lista vacía.
     *
     * @return Lista de DTOs con las razas obtenidas desde la API externa
     */
    @CircuitBreaker(name = "dog-api-breeds", fallbackMethod = "getBreedsFallback")
    public List<DogApiBreedInDTO> getAllDogBreeds() {
        log.debug("Contactando a Dog API para obtener razas...");
        return dogApiClient.getAllBreeds();
    }

    /**
     * Sincroniza las razas de perro desde la API externa con la base de datos local.
     * 
     * Este método implementa una estrategia de sincronización inteligente que:
     * 1. Valida la existencia de la especie 'Perro' en la BD
     * 2. Obtiene las razas desde la API externa
     * 3. Compara con las razas existentes usando externalApiId
     * 4. Guarda únicamente las razas nuevas para evitar duplicados
     * 5. Invalida el caché para reflejar los cambios
     *
     * @return Lista de DTOs de las razas que fueron guardadas/actualizadas
     * @throws IllegalStateException Si la especie 'Perro' no existe en la BD
     */
    @Transactional
    @CacheEvict(value = { "breeds", "breeds-by-specie" }, allEntries = true)
    public List<BreedOutDTO> saveAllDogsBreeds() {
        log.info("Iniciando sincronización de razas de perro desde Dog API");

        // 1. Validar existencia de la especie 'Perro'
        log.debug("Buscando especie 'Perro' en la BBDD...");
        Specie dogSpecie = specieRepository.findByName("Perro")
                .orElseThrow(() -> {
                    log.error("La especie 'Perro' no fue encontrada en la BBDD. No se puede sincronizar.");
                    return new IllegalStateException("Especie 'Perro' no encontrada en la BBDD.");
                });
        log.debug("Especie 'Perro' encontrada con ID: {}", dogSpecie.getId());

        // 2. Obtener razas desde API externa
        List<DogApiBreedInDTO> dogBreeds = getAllDogBreeds();
        if (dogBreeds == null || dogBreeds.isEmpty()) {
            log.info("No se encontraron razas de perro en la API externa.");
            return List.of();
        }
        log.debug("Obtenidas {} razas desde Dog API", dogBreeds.size());

        // 3. Identificar razas existentes en BD por externalApiId
        log.debug("Buscando razas de perro existentes en la BBDD para la especie ID: {}", dogSpecie.getId());
        List<Breed> existingBreeds = breedRepositoryAdapter.findBreedsBySpecieId(dogSpecie.getId());
        Set<String> existingBreedsExternalIds = existingBreeds.stream()
                .map(Breed::getExternalApiId)
                .collect(Collectors.toSet());
        log.debug("Razas existentes encontradas en la BBDD: {}", existingBreedsExternalIds);

        // 4. Filtrar solo las razas nuevas
        log.debug("Filtrando razas nuevas que no existen en la BBDD...");
        List<DogApiBreedInDTO> newApiBreedsFiltered = dogBreeds.stream()
                .filter(dto -> !existingBreedsExternalIds.contains(dto.getId().toString()))
                .toList();
        log.debug("Hay {} razas nuevas", newApiBreedsFiltered.size());

        if (newApiBreedsFiltered.isEmpty()) {
            log.info("No hay razas de perro nuevas para guardar. La BBDD está actualizada.");
            return List.of();
        }

        // 5. Mapear y guardar nuevas razas
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
     * Método fallback ejecutado cuando el Circuit Breaker detecta fallos en la API externa.
     * 
     * Proporciona una respuesta degradada devolviendo una lista vacía, permitiendo
     * que la aplicación continúe funcionando aunque la API externa no esté disponible.
     *
     * @param throwable Excepción que causó la activación del fallback
     * @return Lista vacía como respuesta degradada
     */
    public List<DogApiBreedInDTO> getBreedsFallback(Throwable throwable) {
        log.error("Fallo al obtener razas de perro desde Dog API. Fallback: ", throwable);
        return List.of();
    }
}