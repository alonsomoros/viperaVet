package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

public interface SpecieService {

    List<SpecieOutDTO> getAll();

    SpecieOutDTO findByName(String name);

    boolean existsByName(String name);

}
