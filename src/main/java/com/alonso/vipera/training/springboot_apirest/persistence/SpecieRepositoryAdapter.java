package com.alonso.vipera.training.springboot_apirest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SpecieRepositoryAdapter {

    private final SpecieRepository specieRepository;

    public List<Specie> findAll() {
        return specieRepository.findAll();
    }

    public Optional<Specie> findByName(String name) {
        return specieRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return specieRepository.existsByName(name);
    }

}
