package com.alonso.vipera.training.springboot_apirest.service;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

public interface SpecieService {

    SpecieOutDTO findByName(String name);

    boolean existsByName(String name);

}
