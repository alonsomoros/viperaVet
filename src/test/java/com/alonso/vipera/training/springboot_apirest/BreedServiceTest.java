package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.BreedServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BreedServiceTest {

    private static final Long BREED_ID = 108L;
    private static final String BREED_NAME = "Border Collie";
    private static final Long SPECIE_ID = 1L;
    private static final String SPECIE_NAME = "Perro";
    private static final String EXTERNAL_API_ID = "50";

    private static final SpecieOutDTO SPECIE_OUT_DTO = new SpecieOutDTO(SPECIE_ID, SPECIE_NAME);

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
        specie.setId(SPECIE_ID);
        specie.setName(SPECIE_NAME);

        breed = new Breed(BREED_ID, BREED_NAME, specie, EXTERNAL_API_ID);
        breedOutDTO = new BreedOutDTO(BREED_ID, BREED_NAME, EXTERNAL_API_ID, SPECIE_OUT_DTO);
    }

    @Test
    void testFindByName_whenNameFound_shouldReturnBreed() {
        // Arrange
        when(breedRepositoryAdapter.findByName(BREED_NAME)).thenReturn(Optional.of(breed));
        when(breedMapper.toDTO(breed)).thenReturn(breedOutDTO);

        // Act
        BreedOutDTO result = breedServiceImpl.findByName(BREED_NAME);

        // Assert
        assertNotNull(result);
        assertEquals(BREED_NAME, result.getName());
        assertEquals(BREED_ID, result.getId());

        // Verify
        verify(breedRepositoryAdapter, times(1)).findByName(BREED_NAME);
        verify(breedMapper, times(1)).toDTO(breed);
    }

    @Test
    void testFindByName_whenNameNotFound_shouldThrowBreedNotFoundException() {
        // Arrange
        when(breedRepositoryAdapter.findByName(BREED_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BreedNotFoundException.class, () -> breedServiceImpl.findByName(BREED_NAME));

        // Verify
        verify(breedRepositoryAdapter, times(1)).findByName(BREED_NAME);
        verify(breedMapper, times(0)).toDTO(breed);
    }

    @Test
    void testExistsByName_whenNameExists_shouldReturnTrue() {
        // Arrange
        when(breedRepositoryAdapter.existsByName(BREED_NAME)).thenReturn(true);

        // Act
        boolean exists = breedServiceImpl.existsByName(BREED_NAME);

        // Assert
        assertTrue(exists);

        // Verify
        verify(breedRepositoryAdapter, times(1)).existsByName(BREED_NAME);
    }

    @Test
    void testExistsByName_whenNameDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(breedRepositoryAdapter.existsByName(BREED_NAME)).thenReturn(false);

        // Act
        boolean exists = breedServiceImpl.existsByName(BREED_NAME);

        // Assert
        assertFalse(exists);

        // Verify
        verify(breedRepositoryAdapter, times(1)).existsByName(BREED_NAME);
    }

    @Test
    void testFindBySpecieId_whenBreedsFound_shouldReturnBreedList() {
        // Arrange
        when(breedRepositoryAdapter.findBreedsBySpecieId(SPECIE_ID)).thenReturn(List.of(breed));
        when(breedMapper.toDTO(breed)).thenReturn(breedOutDTO);

        // Act
        List<BreedOutDTO> breeds = breedServiceImpl.findBySpecieId(SPECIE_ID);

        // Assert
        assertNotNull(breeds);
        assertEquals(1, breeds.size());
        assertEquals(BREED_NAME, breeds.get(0).getName());

        // Verify
        verify(breedRepositoryAdapter, times(1)).findBreedsBySpecieId(SPECIE_ID);
        verify(breedMapper, times(1)).toDTO(breed);
    }

    @Test
    void testFindBySpecieId_whenNoBreedsFound_shouldReturnEmptyList() {
        // Arrange
        when(breedRepositoryAdapter.findBreedsBySpecieId(SPECIE_ID)).thenReturn(List.of());

        // Act
        List<BreedOutDTO> breeds = breedServiceImpl.findBySpecieId(SPECIE_ID);

        // Assert
        assertNotNull(breeds);
        assertEquals(0, breeds.size());

        // Verify
        verify(breedRepositoryAdapter, times(1)).findBreedsBySpecieId(SPECIE_ID);
        verify(breedMapper, times(0)).toDTO(breed);
    }
}
