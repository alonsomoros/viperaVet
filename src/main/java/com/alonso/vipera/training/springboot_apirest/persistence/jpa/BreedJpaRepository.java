package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;

/**
 * Repositorio para la gestión de entidades Breed.
 * Proporciona operaciones de acceso a datos para la entidad Breed.
 */
@Repository
public interface BreedJpaRepository extends JpaRepository<Breed, Long> {

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
     * @return Lista de Optional con las razas pertenecientes a la especie especificada
     */
    List<Optional<Breed>> findBreedsBySpecieId(Long specieId);

}
