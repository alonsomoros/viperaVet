package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonso.vipera.training.springboot_apirest.exception.BreedNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.BreedMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.BreedServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BreedServiceTest {

    @Mock
    private BreedRepositoryAdapter breedRepositoryAdapter;

    @Mock
    private BreedMapper breedMapper;

    @InjectMocks
    private BreedServiceImpl breedServiceImpl;

    private Breed breed;
    private BreedOutDTO breedOutDTO;
    private Specie specie;

    @BeforeEach
    void setUp() {
        specie = new Specie();
        specie.setId(1L);
        specie.setName("Dog");

        breed = new Breed(108L, "Border Collie", specie, "50");
        breedOutDTO = new BreedOutDTO(108L, "Border Collie", 1L, "50");
    }

    @Test
    void testGetBreedByName_whenNameFound_shouldReturnBreed() {
        // Arrange
        when(breedRepositoryAdapter.findByName(breed.getName())).thenReturn(Optional.of(breed));
        when(breedMapper.toDTO(breed)).thenReturn(breedOutDTO);

        // Act
        BreedOutDTO result = breedServiceImpl.findByName(breed.getName());

        // Assert
        assertNotNull(result);

        // Verify
        verify(breedRepositoryAdapter, times(1)).findByName(breed.getName());
    }

    @Test
    void testGetBreedByName_whenNameNotFound_shouldReturnBreedNotFoundException() {
        // Arrange
        when(breedRepositoryAdapter.findByName(breed.getName())).thenReturn(Optional.empty());

        // Act
        assertThrows(BreedNotFoundException.class, () -> breedServiceImpl.findByName(breed.getName()));

        // Verify
        verify(breedRepositoryAdapter, times(1)).findByName(breed.getName());
    }

    @Test
    void testExistsByName_whenNameExists_shouldReturnTrue() {
        // Arrange
        when(breedRepositoryAdapter.existsByName(breed.getName())).thenReturn(true);

        // Act
        boolean exists = breedServiceImpl.existsByName(breed.getName());

        // Assert
        assertNotNull(exists);

        // Verify
        verify(breedRepositoryAdapter, times(1)).existsByName(breed.getName());
    }

    @Test
    void testExistsByName_whenNameDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(breedRepositoryAdapter.existsByName(breed.getName())).thenReturn(false);

        // Act
        boolean exists = breedServiceImpl.existsByName(breed.getName());

        // Assert
        assertNotNull(exists);

        // Verify
        verify(breedRepositoryAdapter, times(1)).existsByName(breed.getName());
    }

    @Test
    void testGetBreedsBySpecieId_whenValidResponse_shouldReturnBreedList() {
        // Arrange
        when(breedRepositoryAdapter.findBreedsBySpecieId(specie.getId())).thenReturn(java.util.List.of(breed));
        when(breedMapper.toDTO(breed)).thenReturn(breedOutDTO);

        // Act
        java.util.List<BreedOutDTO> breeds = breedServiceImpl.findBySpecieId(specie.getId());

        // Assert
        assertNotNull(breeds);

        // Verify
        verify(breedRepositoryAdapter, times(1)).findBreedsBySpecieId(specie.getId());
    }

    @Test
    void testGetBreedsBySpecieId_whenNoBreedsFound_shouldReturnEmptyList() {
        // Arrange
        when(breedRepositoryAdapter.findBreedsBySpecieId(specie.getId())).thenReturn(java.util.List.of());

        // Act
        java.util.List<BreedOutDTO> breeds = breedServiceImpl.findBySpecieId(specie.getId());

        // Assert
        assertNotNull(breeds);

        // Verify
        verify(breedRepositoryAdapter, times(1)).findBreedsBySpecieId(specie.getId());
    }

}
