package com.alonso.vipera.training.springboot_apirest.model.pet;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "breeds")
@AllArgsConstructor
@NoArgsConstructor
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "specie_id", nullable = false)
    private Specie specie;

    @Column(name = "external_api_id")
    private Long externalApiId;

    public BreedOutDTO toDTO(){
        return new BreedOutDTO(this.id, this.name, this.specie.getId(), this.externalApiId);
    }

}
