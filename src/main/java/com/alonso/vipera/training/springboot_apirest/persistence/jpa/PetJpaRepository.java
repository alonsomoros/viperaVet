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

@Repository
public interface PetJpaRepository extends JpaRepository<Pet, Long> {

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

        List<Optional<Pet>> findByName(String name);

        List<Optional<Pet>> findByBirthDate(Date birthDate);

        List<Optional<Pet>> findByBreedName(String breed);

        List<Optional<Pet>> findBySpecieName(String specie);

        List<Optional<Pet>> findByUserUsername(String username);

        boolean existsByName(String name);

}
