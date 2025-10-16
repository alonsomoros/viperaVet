package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PetOutDTO {

    private Long id;
    private String name;
    private Date birthDate;
    private String specie;
    private String breed;
    private Double weight;
    private String diet_info;
    private LocalDate createdAt;

}
