package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetUpdateDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

/**
 * Servicio para la gestión de mascotas.
 * Proporciona operaciones CRUD y consultas específicas para entidades Pet.
 */
public interface PetService {

    /**
     * Actualiza la información de una mascota existente.
     *
     * @param petId        ID de la mascota a actualizar.
     * @param petUpdateDTO DTO con los datos a actualizar.
     * @param username     Nombre de usuario del propietario de la mascota.
     * @return DTO con la información actualizada de la mascota.
     */
    PetOutDTO updatePet(Long petId, PetUpdateDTO petUpdateDTO, String username);

    /**
     * Obtiene todas las mascotas de forma paginada.
     *
     * @param pageable Información de paginación (página, tamaño, ordenamiento)
     * @return Página con las mascotas encontradas
     */
    Page<PetOutDTO> getAll(Pageable pageable);

    /**
     * Busca mascotas aplicando múltiples filtros opcionales de forma paginada.
     *
     * @param pet_id    ID de la mascota (opcional)
     * @param name      Nombre de la mascota (opcional)
     * @param breed_id  ID de la raza (opcional)
     * @param specie_id ID de la especie (opcional)
     * @param pageable  Información de paginación (página, tamaño, ordenamiento)
     * @return Página con las mascotas que coinciden con los filtros aplicados
     */
    Page<PetOutDTO> getPetByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable);

    /**
     * Obtiene todas las mascotas asociadas a un usuario específico.
     *
     * @param username Nombre de usuario del propietario
     * @return Lista de mascotas pertenecientes al usuario especificado
     */
    List<PetOutDTO> getPetsByUserUsername(String username);

    /**
     * Busca mascotas por nombre exacto.
     *
     * @param name Nombre de la mascota a buscar
     * @return Lista de mascotas con el nombre especificado
     */
    List<PetOutDTO> getByName(String name);

    /**
     * Busca mascotas por fecha de nacimiento.
     *
     * @param birthDate Fecha de nacimiento a buscar
     * @return Lista de mascotas nacidas en la fecha especificada
     */
    List<PetOutDTO> getByBirthDate(Date birthDate);

    /**
     * Busca mascotas por nombre de raza.
     *
     * @param breed Nombre de la raza a buscar
     * @return Lista de mascotas de la raza especificada
     */
    List<PetOutDTO> getByBreedName(String breed);

    /**
     * Busca mascotas por nombre de especie.
     *
     * @param specie Nombre de la especie a buscar
     * @return Lista de mascotas de la especie especificada
     */
    List<PetOutDTO> getBySpecieName(String specie);

    /**
     * Registra una nueva mascota y la asocia a un usuario existente (el que la
     * creó).
     *
     * @param petInDTO El DTO de entrada con los datos de la nueva mascota (nombre,
     *                 fecha_nacimiento, especie, raza, peso, info_dieta).
     * @param username El nombre de usuario del dueño al que se asociará la mascota.
     * @return El DTO de salida de la mascota con los datos que se quieran mostrar
     *         al cliente. (id, nombre, fecha_nacimiento, peso, info_dieta,
     *         createdAt)
     * @throws UsernameNotFoundException Si el 'username' del dueño no existe.
     * @throws IdNotFoundException       Si el ID de la raza o especie proporcionada
     *                                   no se encuentra.
     */
    PetOutDTO save(PetInDTO petInDTO, String username);

    /**
     * Elimina una mascota por su ID.
     *
     * @param id ID de la mascota a eliminar
     * @throws IdNotFoundException Si no se encuentra una mascota con el ID
     *                             especificado
     */
    void delete(Long id);

}
