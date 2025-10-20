package com.alonso.vipera.training.springboot_apirest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    
    Optional<Breed> findByName(String name);

    boolean existsByName(String name);

    List<Optional<Breed>> findBreedsBySpecieId(Long specieId);

}
