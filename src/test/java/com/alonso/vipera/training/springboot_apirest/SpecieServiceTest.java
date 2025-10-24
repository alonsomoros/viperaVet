package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.SpecieServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SpecieServiceTest {

    @Mock
    private SpecieRepositoryAdapter specieRepositoryAdapter;

    @InjectMocks
    private SpecieServiceImpl specieServiceImpl;

    private Specie specie;
    private SpecieOutDTO specieOutDTO;

    @BeforeEach
    void setUp() {
        specie = new Specie();
        specie.setId(1L);
        specie.setName("Perro");

        specieOutDTO = new SpecieOutDTO(1L, "Perro");
    }

    @Test
    void testGetSpecieByName_whenNameFound_shouldReturnSpecie() {
        // Arrange
        when(specieRepositoryAdapter.findByName(specie.getName())).thenReturn(Optional.of(specie));

        // Act
        SpecieOutDTO result = specieServiceImpl.findByName(specie.getName());

        // Assert
        assertNotNull(result);
        assertEquals(specieOutDTO, result);

        // Verify
        verify(specieRepositoryAdapter, times(1)).findByName(specie.getName());
    }

    @Test
    void testGetSpecieByName_whenNameNotFound_shouldReturnSpecieNotFoundException() {
        // Arrange
        when(specieRepositoryAdapter.findByName(specie.getName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> specieServiceImpl.findByName(specie.getName()));

        // Verify
        verify(specieRepositoryAdapter, times(1)).findByName(specie.getName());
    }

    @Test
    void testExistsByName_whenNameExists_shouldReturnTrue() {
        // Arrange
        when(specieRepositoryAdapter.existsByName(specie.getName())).thenReturn(true);

        // Act
        boolean exists = specieServiceImpl.existsByName(specie.getName());

        // Assert
        assertEquals(true, exists);

        // Verify
        verify(specieRepositoryAdapter, times(1)).existsByName(specie.getName());
    }

    @Test
    void testExistsByName_whenNameDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(specieRepositoryAdapter.existsByName(specie.getName())).thenReturn(false);

        // Act
        boolean exists = specieServiceImpl.existsByName(specie.getName());

        // Assert
        assertEquals(false, exists);

        // Verify
        verify(specieRepositoryAdapter, times(1)).existsByName(specie.getName());
    }

}
