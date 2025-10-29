package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
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

/**
 * Servicio especializado para la sincronización de razas de gato desde la Cat API externa.
 * 
 * Este servicio se encarga de obtener datos de razas de gato desde una API externa,
 * compararlas con los datos existentes en la base de datos local, y sincronizar
 * únicamente las razas nuevas para evitar duplicados.
 * 
 * Implementa patrones de Circuit Breaker para tolerancia a fallos y manejo de caché
 * para optimizar el rendimiento de las consultas posteriores.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CatApiBreedsService {

    private final CatApiClient catApiClient;
    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final SpecieRepositoryAdapter specieRepository;
    private final BreedMapper breedMapper;

    /**
     * Obtiene todas las razas de gato desde la Cat API externa.
     * 
     * Implementa el patrón Circuit Breaker para manejar fallos de la API externa.
     * En caso de fallo, se ejecutará el método fallback que retorna una lista vacía.
     *
     * @return Lista de DTOs con las razas obtenidas desde la API externa
     */
    @CircuitBreaker(name = "cat-api-breeds", fallbackMethod = "getBreedsFallback")
    public List<CatApiBreedInDTO> getAllCatBreeds() {
        log.debug("Contactando a Cat API para obtener razas...");
        return catApiClient.getAllBreeds();
    }

    /**
     * Sincroniza las razas de gato desde la API externa con la base de datos local.
     * 
     * Este método implementa una estrategia de sincronización inteligente que:
     * 1. Valida la existencia de la especie 'Gato' en la BD
     * 2. Obtiene las razas desde la API externa
     * 3. Compara con las razas existentes usando externalApiId
     * 4. Guarda únicamente las razas nuevas para evitar duplicados
     * 5. Invalida el caché para reflejar los cambios
     * 
     * Nota: La Cat API utiliza String como ID (a diferencia de Dog API que usa Integer),
     * por lo que no requiere conversión toString() en la comparación.
     *
     * @return Lista de DTOs de las razas que fueron guardadas/actualizadas
     * @throws IllegalStateException Si la especie 'Gato' no existe en la BD
     */
    @Transactional
    @CacheEvict(value = { "breeds", "breeds-by-specie" }, allEntries = true)
    public List<BreedOutDTO> saveAllCatsBreeds() {
        log.info("Iniciando sincronización de razas de gato desde Cat API");

        // 1. Validar existencia de la especie 'Gato'
        log.debug("Buscando especie 'Gato' en la BBDD...");
        Specie catSpecie = specieRepository.findByName("Gato")
                .orElseThrow(() -> {
                    log.error("La especie 'Gato' no fue encontrada en la BBDD. No se puede sincronizar.");
                    return new IllegalStateException("Especie 'Gato' no encontrada en la BBDD.");
                });
        log.debug("Especie 'Gato' encontrada con ID: {}", catSpecie.getId());

        // 2. Obtener razas desde API externa
        List<CatApiBreedInDTO> catBreeds = getAllCatBreeds();
        if (catBreeds == null || catBreeds.isEmpty()) {
            log.info("No se encontraron razas de gato en la API externa.");
            return List.of();
        }
        log.debug("Obtenidas {} razas desde Cat API: {}", catBreeds.size());

        // 3. Identificar razas existentes en BD por externalApiId
        log.debug("Buscando razas de gato existentes en la BBDD para la especie ID: {}", catSpecie.getId());
        List<Breed> existingBreeds = breedRepositoryAdapter.findBreedsBySpecieId(catSpecie.getId());
        Set<String> existingBreedsExternalIds = existingBreeds.stream()
                .map(Breed::getExternalApiId)
                .collect(Collectors.toSet());
        log.debug("Razas existentes encontradas en la BBDD: {}", existingBreedsExternalIds);

        // 4. Filtrar solo las razas nuevas (Cat API ya usa String como ID)
        log.debug("Filtrando razas nuevas que no existen en la BBDD...");
        List<CatApiBreedInDTO> newApiBreedsFiltered = catBreeds.stream()
                .filter(dto -> !existingBreedsExternalIds.contains(dto.getId()))
                .toList();
        log.debug("Hay {} razas nuevas", newApiBreedsFiltered.size());

        if (newApiBreedsFiltered.isEmpty()) {
            log.info("No hay razas de gato nuevas para guardar. La BBDD está actualizada.");
            return List.of();
        }

        // 5. Mapear y guardar nuevas razas
        List<Breed> breedsToSave = breedMapper.fromCatApiToEntity(newApiBreedsFiltered, catSpecie);

        log.debug("Guardando/actualizando {} razas de gato en la BBDD...", breedsToSave.size());
        List<BreedOutDTO> savedBreeds = breedRepositoryAdapter.saveAllBreeds(breedsToSave)
                .stream()
                .map(breedMapper::toDTO)
                .toList();

        log.info("Sincronización completada: {} razas de gato guardadas/actualizadas", savedBreeds.size());
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
    public List<CatApiBreedInDTO> getBreedsFallback(Throwable throwable) {
        log.error("Fallo al obtener razas de gato desde Cat API. Fallback: ", throwable);
        return List.of();
    }
}