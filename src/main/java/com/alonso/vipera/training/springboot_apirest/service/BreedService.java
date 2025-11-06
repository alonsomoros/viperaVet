package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.exception.BreedNotFoundException;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;

/**
 * Servicio para la gestión de razas de mascotas.
 * Proporciona operaciones de consulta para entidades Breed.
 */
public interface BreedService {

    /**
     * Obtiene todas las razas disponibles.
     *
     * @return Lista de todas las razas registradas en el sistema
     */
    List<BreedOutDTO> getAllBreeds();

    /**
     * Busca una raza por su nombre exacto.
     *
     * @param name Nombre de la raza a buscar
     * @return DTO de la raza encontrada
     * @throws BreedNotFoundException Si no se encuentra una raza con el nombre especificado
     */
    BreedOutDTO findByName(String name);

    /**
     * Verifica si existe una raza con el nombre especificado.
     *
     * @param name Nombre de la raza a verificar
     * @return true si existe una raza con ese nombre, false en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Obtiene todas las razas pertenecientes a una especie específica.
     *
     * @param id ID de la especie para filtrar las razas
     * @return Lista de razas pertenecientes a la especie especificada
     * @throws BreedNotFoundException Si no se encuentra una especie con el ID especificado
     */
    List<BreedOutDTO> findBySpecieId(Long id);

}