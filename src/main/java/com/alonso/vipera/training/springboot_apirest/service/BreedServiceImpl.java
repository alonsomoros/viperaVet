package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepositoryAdapter breedRepositoryAdapter;

    @Override
    public BreedOutDTO findByName(String name) {
        return breedRepositoryAdapter.findByName(name).get().toDTO();
    }

    @Override
    public boolean existsByName(String name) {
        return breedRepositoryAdapter.existsByName(name);
    }
    @Override
    public List<BreedOutDTO> findBySpecieId(Long id) {
        return breedRepositoryAdapter.findBreedsBySpecieId(id)
                .stream()
                .map(Breed::toDTO)
                .collect(Collectors.toList());
    }

}
