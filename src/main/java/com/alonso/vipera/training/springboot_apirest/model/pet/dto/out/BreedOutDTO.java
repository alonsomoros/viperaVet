package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BreedOutDTO {
    private Long id;
    private String name;
    private Long specieId;
    private String externalApiId;
}
