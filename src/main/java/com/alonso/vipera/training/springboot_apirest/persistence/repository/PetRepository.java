package com.alonso.vipera.training.springboot_apirest.persistence.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

/**
 * Repositorio para la gestión de mascotas.
 */
public interface PetRepository {

    /**
     * Busca una mascota por su ID.
     * 
     * @param id ID de la mascota.
     * @return Optional que contiene la mascota si se encuentra, o vacío si no.
     */
    Optional<Pet> findById(Long id);

    /**
     * Busca mascotas por el nombre de su dueño.
     * 
     * @param username Nombre de usuario del dueño.
     * @return Lista de mascotas pertenecientes al dueño especificado.
     */
    List<Pet> findPetsByOwnerUsername(String username);

    /**
     * Busca mascotas por su nombre.
     * 
     * @param name Nombre de la mascota.
     * @return Lista de mascotas con el nombre especificado.
     */
    List<Pet> findByName(String name);

    /**
     * Busca mascotas por su fecha de nacimiento.
     * 
     * @param birthDate Fecha de nacimiento de la mascota.
     * @return Lista de mascotas nacidas en la fecha especificada.
     */
    List<Pet> findByBirthDate(Date birthDate);

    /**
     * Busca mascotas por el nombre de su raza.
     * 
     * @param breed Nombre de la raza.
     * @return Lista de mascotas de la raza especificada.
     */
    List<Pet> findByBreedName(String breed);

    /**
     * Busca mascotas por el nombre de su especie.
     * 
     * @param specie Nombre de la especie.
     * @return Lista de mascotas de la especie especificada.
     */
    List<Pet> findBySpecieName(String specie);

    /**
     * Recupera todas las mascotas con paginación.
     * 
     * @param pageable Parámetros de paginación.
     * @return Página de mascotas.
     */
    Page<Pet> findAll(Pageable pageable);

    /**
     * Busca mascotas según varios filtros.
     * 
     * @param pet_id    ID de la mascota (opcional).
     * @param name      Nombre de la mascota (opcional).
     * @param breed_id  ID de la raza (opcional).
     * @param specie_id ID de la especie (opcional).
     * @param pageable  Parámetros de paginación.
     * @return Página de mascotas que coinciden con los filtros.
     */
    Page<Pet> findByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable);

    /**
     * Guarda una mascota en el repositorio.
     * 
     * @param pet Mascota a guardar.
     * @return Mascota guardada.
     */
    Pet save(Pet pet);

    /**
     * Elimina una mascota del repositorio.
     * 
     * @param pet Mascota a eliminar.
     */
    void delete(Pet pet);

}
