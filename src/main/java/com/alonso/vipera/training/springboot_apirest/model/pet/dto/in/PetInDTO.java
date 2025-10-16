package com.alonso.vipera.training.springboot_apirest.model.pet.dto.in;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PetInDTO {

    private String name;
    private Date birthDate;
    private String specie;
    private String breed;
    private Double weight;
    private String diet_info;
    
}
