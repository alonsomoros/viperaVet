package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.SpecieNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.SpecieMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecieServiceImpl implements SpecieService {

    private final SpecieRepositoryAdapter specieRepositoryAdapter;
    private final SpecieMapper specieMapper;

    @Override
    @Cacheable("species")
    public List<SpecieOutDTO> getAll() {
        log.debug("Recuperando todos las razas de la base de datos...");
        List<SpecieOutDTO> species = specieRepositoryAdapter.findAll()
                .stream()
                .map(specieMapper::toDTO)
                .toList();
        log.debug("Se han recuperado {} razas en total.", species.size());
        return species;
    }

    @Override
    public SpecieOutDTO findByName(String name) {
        log.debug("Buscando especie por nombre: {}", name);
        SpecieOutDTO specie = specieRepositoryAdapter.findByName(name).orElseThrow(() -> new SpecieNotFoundException())
                .toDto();
        log.debug("Especie encontrada: {}", specie);
        return specie;
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("Comprobando existencia de especie por nombre: {}", name);
        boolean exists = specieRepositoryAdapter.existsByName(name);
        log.debug("¿Existe la especie {}? {}", name, exists);
        return exists;
    }

}
