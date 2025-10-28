package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;

public interface BreedService {

    List<BreedOutDTO> getAllBreeds();

    BreedOutDTO findByName(String name);

    boolean existsByName(String name);

    List<BreedOutDTO> findBySpecieId(Long id);

}
