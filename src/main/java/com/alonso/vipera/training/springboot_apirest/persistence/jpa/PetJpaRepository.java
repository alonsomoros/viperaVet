package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

/**
 * Repositorio JPA para la entidad Pet.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas
 * relacionadas con las mascotas en la base de datos.
 */
@Repository
public interface PetJpaRepository extends JpaRepository<Pet, Long> {

        /**
         * Busca mascotas aplicando múltiples filtros opcionales.
         * Permite filtrar por ID de mascota, nombre, ID de raza e ID de especie.
         * Si un filtro es nulo, no se aplica en la consulta.
         *
         * @param petId    ID de la mascota (opcional)
         * @param name     Nombre de la mascota (opcional)
         * @param breedId  ID de la raza (opcional)
         * @param specieId ID de la especie (opcional)
         * @param pageable Información de paginación
         * @return Página de mascotas que cumplen con los filtros especificados
         */
        @Query("SELECT p FROM Pet p WHERE " +
                        "(:pet_id IS NULL OR p.id = :pet_id) AND " +
                        "(:name IS NULL OR p.name LIKE %:name%) AND " +
                        "(:breedId IS NULL OR p.breed.id = :breedId) AND " +
                        "(:specieId IS NULL OR p.specie.id = :specieId)")
        Page<Pet> findByFilters(
                        @Param("pet_id") Long petId,
                        @Param("name") String name,
                        @Param("breedId") Long breedId,
                        @Param("specieId") Long specieId,
                        Pageable pageable);

        /**
         * Busca mascotas por su nombre.
         * 
         * @param name Nombre de la mascota a buscar
         * @return Lista de mascotas que coinciden con el nombre proporcionado
         */
        List<Optional<Pet>> findByName(String name);

        /**
         * Busca mascotas por su fecha de nacimiento.
         * 
         * @param birthDate Fecha de nacimiento de la mascota a buscar
         * @return Lista de mascotas que coinciden con la fecha de nacimiento
         *         proporcionada
         */
        List<Optional<Pet>> findByBirthDate(Date birthDate);

        /**
         * Busca mascotas por el nombre de su raza.
         * 
         * @param breed Nombre de la raza de la mascota a buscar
         * @return Lista de mascotas que coinciden con el nombre de raza proporcionado
         */
        List<Optional<Pet>> findByBreedName(String breed);

        /**
         * Busca mascotas por el nombre de su especie.
         * 
         * @param specie Nombre de la especie de la mascota a buscar
         * @return Lista de mascotas que coinciden con el nombre de especie
         *         proporcionado
         */
        List<Optional<Pet>> findBySpecieName(String specie);

        /**
         * Busca mascotas por el nombre de usuario del propietario.
         * 
         * @param username Nombre de usuario del propietario de la mascota
         * @return Lista de mascotas que pertenecen al usuario especificado
         */
        List<Optional<Pet>> findByUserUsername(String username);

        /**
         * Verifica si existe una mascota con el nombre especificado.
         * 
         * @param name Nombre de la mascota a verificar
         * @return true si existe una mascota con el nombre dado, false en caso
         *         contrario
         */
        boolean existsByName(String name);

}
