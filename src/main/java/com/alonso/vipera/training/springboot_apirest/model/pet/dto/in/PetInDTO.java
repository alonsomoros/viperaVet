package com.alonso.vipera.training.springboot_apirest.model.pet.dto.in;

import java.sql.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PetInDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private Date birthDate;

    @NotBlank(message = "La especie no puede estar vacía")
    @Size(min = 2, max = 30, message = "La especie debe tener entre 2 y 30 caracteres")
    private String specie;

    @NotBlank(message = "La raza no puede estar vacía")
    @Size(min = 2, max = 30, message = "La raza debe tener entre 2 y 30 caracteres")
    private String breed;

    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0.1 kg")
    private Double weight;

    @Size(max = 255, message = "La información de dieta no puede exceder los 255 caracteres")
    private String diet_info;

}
