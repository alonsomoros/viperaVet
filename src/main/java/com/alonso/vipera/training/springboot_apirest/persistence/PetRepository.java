package com.alonso.vipera.training.springboot_apirest.persistence;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Optional<Pet>> findByName(String name);

    List<Optional<Pet>> findByBirthDate(Date birthDate);

    List<Optional<Pet>> findByBreed(String breed);

    List<Optional<Pet>> findBySpecie(String specie);

    List<Optional<Pet>> findByUserUsername(String username);

    boolean existsByName(String name);

}
