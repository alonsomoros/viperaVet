package com.alonso.vipera.training.springboot_apirest.model.pet.dto.in;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos necesarios para registrar una nueva mascota en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetInDTO {

    @Schema(description = "Nombre de la mascota. Debe tener entre 2 y 50 caracteres.", example = "Max", required = true)
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Schema(description = "Fecha de nacimiento de la mascota.", example = "2020-05-15", required = true)
    @NotNull
    private Date birthDate;

    @Schema(description = "ID de la especie de la mascota.", example = "1 (Perro)", required = true)
    @NotNull
    @Positive
    private Long specieId;

    @Schema(description = "ID de la raza de la mascota.", example = "1", required = true)
    @NotNull
    @Positive
    private Long breedId;

    @Schema(description = "Peso de la mascota en kilogramos.", example = "15.5", minimum = "0.1", maximum = "200.0")
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "200.0")
    private Double weight;

    @Schema(description = "Información sobre la dieta especial de la mascota.", example = "Comida sin gluten, 3 veces al día")
    @Size(max = 500)
    private String dietInfo;
}