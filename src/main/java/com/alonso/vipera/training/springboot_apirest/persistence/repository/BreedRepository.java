package com.alonso.vipera.training.springboot_apirest.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;

/**
 * Repositorio para la gestión de razas de mascotas.
 */
public interface BreedRepository {

    /**
     * 
     * Obtiene todas las razas disponibles.
     *
     * @return Lista de todas las razas
     */
    List<Breed> findAll();

    /**
     * 
     * Busca una raza por su ID.
     *
     * @param id ID de la raza a buscar
     * @return Optional que contiene la raza encontrada o vacío si no existe
     */
    Optional<Breed> findById(Long id);

    /**
     * 
     * Busca una raza por su nombre exacto.
     *
     * @param name Nombre de la raza a buscar
     * @return Optional que contiene la raza encontrada o vacío si no existe
     */
    Optional<Breed> findByName(String name);

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
     * @param specieId ID de la especie para filtrar las razas
     * @return Lista de razas pertenecientes a la especie especificada
     */
    List<Breed> findBreedsBySpecieId(Long specieId);

    /**
     * Guarda una lista de razas.
     *
     * @param breeds Lista de razas a guardar
     * @return Lista de razas guardadas
     */
    List<Breed> saveAllBreeds(List<Breed> breeds);

}
