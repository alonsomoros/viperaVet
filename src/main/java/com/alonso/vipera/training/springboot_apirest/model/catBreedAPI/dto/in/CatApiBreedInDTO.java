package com.alonso.vipera.training.springboot_apirest.model.catBreedAPI.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatApiBreedInDTO {
    private String id;
    private String name;
    private String description;
    private String life_span;
    private String temperament;
    private String origin;
}