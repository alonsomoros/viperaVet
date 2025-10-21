package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecieServiceImpl implements SpecieService {

    private final SpecieRepositoryAdapter specieRepositoryAdapter;

    @Override
    public SpecieOutDTO findByName(String name){
        return specieRepositoryAdapter.findByName(name).get().tDto();
    }

    @Override
    public boolean existsByName(String name){
        return specieRepositoryAdapter.existsByName(name);
    }

}
