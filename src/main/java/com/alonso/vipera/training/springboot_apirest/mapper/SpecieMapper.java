package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

@Component
public class SpecieMapper {

    public SpecieOutDTO toDTO(Specie specie) {
        return new SpecieOutDTO(specie.getId(), specie.getName());
    }

}
