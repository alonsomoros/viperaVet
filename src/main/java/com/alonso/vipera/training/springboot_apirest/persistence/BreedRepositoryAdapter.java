package com.alonso.vipera.training.springboot_apirest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BreedRepositoryAdapter {

    private final BreedRepository breedRepository;

    public List<Breed> findAll() {
        return breedRepository.findAll();
    }

    public Optional<Breed> findById(Long id) {
        return breedRepository.findById(id);
    }

    public Optional<Breed> findByName(String name) {
        return breedRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return breedRepository.existsByName(name);
    }

    public List<Breed> findBreedsBySpecieId(Long specieId) {
        return breedRepository.findBreedsBySpecieId(specieId)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Breed save(Breed breed) {
        return breedRepository.save(breed);
    }

    public List<Breed> saveAllBreeds(List<Breed> breeds) {
        return breedRepository.saveAll(breeds);
    }

}
