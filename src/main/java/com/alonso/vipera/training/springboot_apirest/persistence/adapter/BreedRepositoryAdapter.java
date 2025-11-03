package com.alonso.vipera.training.springboot_apirest.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.BreedJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.BreedRepository;

import lombok.AllArgsConstructor;

/**
 * Adaptador para el repositorio de Breed, proporcionando una capa de abstracción
 * sobre las operaciones de acceso a datos relacionadas con la entidad Breed.
 */
@Component
@AllArgsConstructor
public class BreedRepositoryAdapter implements BreedRepository {

    private final BreedJpaRepository breedRepository;

    @Override
    public List<Breed> findAll() {
        return breedRepository.findAll();
    }

    @Override
    public Optional<Breed> findById(Long id) {
        return breedRepository.findById(id);
    }

    @Override
    public Optional<Breed> findByName(String name) {
        return breedRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return breedRepository.existsByName(name);
    }

    @Override
    public List<Breed> findBreedsBySpecieId(Long specieId) {
        return breedRepository.findBreedsBySpecieId(specieId)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Breed> saveAllBreeds(List<Breed> breeds) {
        return breedRepository.saveAll(breeds);
    }

}
