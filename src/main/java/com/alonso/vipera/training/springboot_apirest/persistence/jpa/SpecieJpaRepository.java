package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;

/**
 * Repositorio JPA para la entidad Specie.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas
 * relacionadas con las especies de mascotas en la base de datos.
 */
@Repository
public interface SpecieJpaRepository extends JpaRepository<Specie, Long> {

    /**
     * Busca una especie por su nombre.
     * 
     * @param name Nombre de la especie a buscar
     * @return Optional que contiene la especie encontrada o vacío si no existe
     */
    Optional<Specie> findByName(String name);

    /**
     * Verifica si existe una especie con el nombre proporcionado.
     * 
     * @param name Nombre de la especie a verificar
     * @return true si existe una especie con el nombre dado, false en caso
     *         contrario
     */
    boolean existsByName(String name);

}
