package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.BreedNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final BreedMapper breedMapper;

    @Override
    @Cacheable("breeds")
    public List<BreedOutDTO> getAllBreeds() {
        log.debug("Recuperando todos las razas de la base de datos...");
        List<BreedOutDTO> breeds = breedRepositoryAdapter.findAll()
                .stream()
                .map(breedMapper::toDTO)
                .toList();
        log.debug("Se han recuperado {} razas en total.", breeds.size());
        return breeds;
    }

    @Override
    public BreedOutDTO findByName(String name) {
        log.debug("Buscando raza por nombre: {}", name);
        BreedOutDTO breed = breedRepositoryAdapter.findByName(name)
                .map(breedMapper::toDTO)
                .orElseThrow(() -> new BreedNotFoundException());
        log.debug("Raza encontrada: {}", breed != null ? breed.getName() : "Ninguna");
        return breed;
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("Verificando existencia de raza por nombre: {}", name);
        boolean exists = breedRepositoryAdapter.existsByName(name);
        log.debug("¿Existe la raza '{}'? {}", name, exists);
        return exists;
    }

    @Override
    @Cacheable(value = "breeds-by-specie", key = "#id")
    public List<BreedOutDTO> findBySpecieId(Long id) {
        log.debug("Buscando razas por ID de especie: {}", id);
        List<BreedOutDTO> breeds = breedRepositoryAdapter.findBreedsBySpecieId(id)
                .stream()
                .map(breedMapper::toDTO)
                .collect(Collectors.toList());
        log.debug("Número de razas encontradas para la especie ID {}: {}", id, breeds.size());
        return breeds;
    }

}
