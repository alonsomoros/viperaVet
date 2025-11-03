package com.alonso.vipera.training.springboot_apirest.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.SpecieJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.SpecieRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SpecieRepositoryAdapter implements SpecieRepository {

    private final SpecieJpaRepository specieRepository;

    @Override
    public List<Specie> findAll() {
        return specieRepository.findAll();
    }

    @Override
    public Optional<Specie> findById(Long id) {
        return specieRepository.findById(id);
    }

    @Override
    public Optional<Specie> findByName(String name) {
        return specieRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return specieRepository.existsByName(name);
    }

}
