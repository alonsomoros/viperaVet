package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;

@Repository
public interface SpecieJpaRepository extends JpaRepository<Specie, Long> {

    Optional<Specie> findByName(String name);

    boolean existsByName(String name);

}
