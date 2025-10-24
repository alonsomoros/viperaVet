package com.alonso.vipera.training.springboot_apirest.model.pet;

import java.util.Set;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "species")
@AllArgsConstructor
@NoArgsConstructor
public class Specie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "specie")
    private Set<Breed> breeds;

    public SpecieOutDTO toDto() {
        return new SpecieOutDTO(this.id, this.name);
    }

}