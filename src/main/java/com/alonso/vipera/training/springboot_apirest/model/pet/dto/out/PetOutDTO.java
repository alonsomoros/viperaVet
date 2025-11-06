package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import java.sql.Date;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la salida de información completa de una mascota.
 */
@Schema(description = "Información completa de una mascota.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetOutDTO {

    @Schema(description = "ID único de la mascota.", example = "1")
    private Long id;

    @Schema(description = "Nombre de la mascota.", example = "Max")
    private String name;

    @Schema(description = "Fecha de nacimiento de la mascota.", example = "2020-05-15")
    private Date birthDate;

    @Schema(description = "Peso de la mascota en kilogramos.", example = "15.5")
    private Double weight;

    @Schema(description = "Información sobre la dieta de la mascota.", example = "Comida sin gluten, 3 veces al día")
    private String dietInfo;

    @Schema(description = "Información de la especie de la mascota.")
    private SpecieOutDTO specie;

    @Schema(description = "Información de la raza de la mascota.")
    private BreedOutDTO breed;

    @Schema(description = "Fecha y hora de registro de la mascota.", example = "2024-10-29T10:30:00")
    private LocalDateTime createdAt;
}