package com.alonso.vipera.training.springboot_apirest.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;

/**
 * Repositorio para la gestión de especies de mascotas.
 */
public interface SpecieRepository {

    /**
     * 
     * Obtiene todas las especies disponibles.
     *
     * @return Lista de todas las especies
     */
    List<Specie> findAll();

    /**
     * Busca una especie por su ID.
     *
     * @param id ID de la especie a buscar
     * @return Optional que contiene la especie encontrada o vacío si no existe
     */
    Optional<Specie> findById(Long id);

    /**
     * Busca una especie por su nombre.
     *
     * @param name Nombre de la especie a buscar
     * @return Optional que contiene la especie encontrada o vacío si no existe
     */
    Optional<Specie> findByName(String name);

    /**
     * Verifica si existe una especie con el nombre especificado.
     *
     * @param name Nombre de la especie a verificar
     * @return true si existe una especie con ese nombre, false en caso contrario
     */
    boolean existsByName(String name);

}
