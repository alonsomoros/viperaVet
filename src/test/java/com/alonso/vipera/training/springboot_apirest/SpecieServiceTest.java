package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.alonso.vipera.training.springboot_apirest.mapper.SpecieMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.SpecieRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.SpecieServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SpecieServiceTest {

    private static final String SPECIE_NAME = "Perro";
    private static final Long SPECIE_ID = 1L;

    @Mock
    private SpecieRepositoryAdapter specieRepositoryAdapter;

    @Mock
    private SpecieMapper specieMapper;

    @InjectMocks
    private SpecieServiceImpl specieServiceImpl;

    private Specie specie;
    private SpecieOutDTO specieOutDTO;

    @BeforeEach
    void setUp() {
        specie = new Specie();
        specie.setId(SPECIE_ID);
        specie.setName(SPECIE_NAME);

        specieOutDTO = new SpecieOutDTO(SPECIE_ID, SPECIE_NAME);
    }

    @Test
    void testFindByName_whenNameFound_shouldReturnSpecie() {
        // Arrange
        when(specieRepositoryAdapter.findByName(SPECIE_NAME)).thenReturn(Optional.of(specie));
        when(specieMapper.toDTO(specie)).thenReturn(specieOutDTO);

        // Act
        SpecieOutDTO result = specieServiceImpl.findByName(SPECIE_NAME);

        // Assert
        assertNotNull(result);
        assertEquals(SPECIE_NAME, result.getName());
        assertEquals(SPECIE_ID, result.getId());

        // Verify
        verify(specieRepositoryAdapter, times(1)).findByName(SPECIE_NAME);
    }

    @Test
    void testFindByName_whenNameNotFound_shouldThrowRuntimeException() {
        // Arrange
        when(specieRepositoryAdapter.findByName(SPECIE_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> specieServiceImpl.findByName(SPECIE_NAME));

        // Verify
        verify(specieRepositoryAdapter, times(1)).findByName(SPECIE_NAME);
    }

    @Test
    void testExistsByName_whenNameExists_shouldReturnTrue() {
        // Arrange
        when(specieRepositoryAdapter.existsByName(SPECIE_NAME)).thenReturn(true);

        // Act
        boolean exists = specieServiceImpl.existsByName(SPECIE_NAME);

        // Assert
        assertTrue(exists);

        // Verify
        verify(specieRepositoryAdapter, times(1)).existsByName(SPECIE_NAME);
    }

    @Test
    void testExistsByName_whenNameDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(specieRepositoryAdapter.existsByName(SPECIE_NAME)).thenReturn(false);

        // Act
        boolean exists = specieServiceImpl.existsByName(SPECIE_NAME);

        // Assert
        assertFalse(exists);

        // Verify
        verify(specieRepositoryAdapter, times(1)).existsByName(SPECIE_NAME);
    }
}
