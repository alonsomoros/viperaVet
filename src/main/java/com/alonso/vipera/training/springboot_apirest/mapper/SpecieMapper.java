package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

/**
 * Mapper para convertir entre entidades Specie y sus DTOs correspondientes.
 */
@Component
public class SpecieMapper {

    /**
     * Convierte una entidad Specie a su correspondiente DTO de salida.
     *
     * @param specie Entidad Specie a convertir.
     * @return DTO de salida SpecieOutDTO.
     */
    public SpecieOutDTO toDTO(Specie specie) {
        return new SpecieOutDTO(specie.getId(), specie.getName());
    }

}
