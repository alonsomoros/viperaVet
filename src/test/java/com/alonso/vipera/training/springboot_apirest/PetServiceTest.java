package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.PetMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.PetRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.SpecieRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.PetServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    private static final String USERNAME = "Alonso";
    private static final String PET_NAME = "Obi";
    private static final Long BREED_ID = 50L;
    private static final String BREED_NAME = "Border Collie";
    private static final Long SPECIE_ID = 1L;
    private static final String SPECIE_NAME = "Perro";
    private static final Date BIRTH_DATE = Date.valueOf("2020-01-01");
    private static final Double WEIGHT = 23.0;
    private static final String DIET_INFO = "Dieta equilibrada (a base de salchichas)";
    private static final Long PET_ID = 1L;

    @Mock
    private PetRepositoryAdapter petRepositoryAdapter;

    @Mock
    private UserRepositoryAdapter userRepositoryAdapter;

    @Mock
    private SpecieRepositoryAdapter specieRepositoryAdapter;

    @Mock
    private BreedRepositoryAdapter breedRepositoryAdapter;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petServiceImpl;

    private Pageable testPageable;

    private PetInDTO petInDTO;
    private Pet pet;
    private PetOutDTO petOutDTO;
    private User user;
    private Specie specie;
    private Breed breed;

    @BeforeEach
    public void setUp() {
        testPageable = PageRequest.of(0, 10);

        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        specie = new Specie();
        specie.setId(1L);
        specie.setName("Perro");

        breed = new Breed();
        breed.setId(108L);
        breed.setName("Border Collie");
        breed.setSpecie(specie);
        breed.setExternalApiId("50");

        petInDTO = new PetInDTO(PET_NAME, BIRTH_DATE, SPECIE_ID, BREED_ID, WEIGHT, DIET_INFO);

        pet = new Pet();
        pet.setId(PET_ID);
        pet.setName(PET_NAME);
        pet.setBirthDate(BIRTH_DATE);
        pet.setSpecie(specie);
        pet.setBreed(breed);
        pet.setWeight(WEIGHT);
        pet.setDietInfo(DIET_INFO);
        pet.setUser(user);

        petOutDTO = new PetOutDTO(PET_ID, PET_NAME, BIRTH_DATE, WEIGHT, DIET_INFO, null, null, null);
    }

    @Test
    public void testGetPetsByOwnerUsername_whenPetsFound_returnPageOfPets() {
        // Arrange
        when(petRepositoryAdapter.findPetsByOwnerUsername(USERNAME)).thenReturn(List.of(pet));
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        List<PetOutDTO> pets = petServiceImpl.getPetsByOwnerUsername(USERNAME);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.size());
        assertEquals(PET_NAME, pets.get(0).getName());

        // Verify
        verify(petRepositoryAdapter, times(1)).findPetsByOwnerUsername(USERNAME);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetPetsByOwnerUsername_whenPetsNotFound_returnEmptyList() {
        // Arrange
        when(petRepositoryAdapter.findPetsByOwnerUsername(USERNAME)).thenReturn(List.of());

        // Act
        List<PetOutDTO> pets = petServiceImpl.getPetsByOwnerUsername(USERNAME);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findPetsByOwnerUsername(USERNAME);
    }

    @Test
    public void testGetPetsByName_whenPetsFound_returnListOfPets() {
        // Arrange
        when(petRepositoryAdapter.findByName(PET_NAME)).thenReturn(List.of(pet));
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByName(PET_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.size());
        assertEquals(PET_NAME, pets.get(0).getName());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByName(PET_NAME);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetPetsByName_whenPetsNotFound_returnEmptyList() {
        // Arrange
        when(petRepositoryAdapter.findByName(PET_NAME)).thenReturn(List.of());

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByName(PET_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByName(PET_NAME);
    }

    @Test
    public void testGetByBirthDate_whenPetsFound_returnListOfPets() {
        // Arrange
        when(petRepositoryAdapter.findByBirthDate(BIRTH_DATE)).thenReturn(List.of(pet));
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByBirthDate(BIRTH_DATE);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.size());
        assertEquals(BIRTH_DATE, pets.get(0).getBirthDate());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByBirthDate(BIRTH_DATE);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetByBirthDate_whenPetsNotFound_returnEmptyList() {
        // Arrange
        when(petRepositoryAdapter.findByBirthDate(BIRTH_DATE)).thenReturn(List.of());

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByBirthDate(BIRTH_DATE);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByBirthDate(BIRTH_DATE);
    }

    @Test
    public void testGetByBreedName_whenPetsFound_returnListOfPets() {
        // Arrange
        when(petRepositoryAdapter.findByBreedName(BREED_NAME)).thenReturn(List.of(pet));
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByBreedName(BREED_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByBreedName(BREED_NAME);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetByBreedName_whenPetsNotFound_returnEmptyList() {
        // Arrange
        when(petRepositoryAdapter.findByBreedName(BREED_NAME)).thenReturn(List.of());

        // Act
        List<PetOutDTO> pets = petServiceImpl.getByBreedName(BREED_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findByBreedName(BREED_NAME);
    }

    @Test
    public void testGetBySpecieName_whenPetsFound_returnListOfPets() {
        // Arrange
        when(petRepositoryAdapter.findBySpecieName(SPECIE_NAME)).thenReturn(List.of(pet));
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        List<PetOutDTO> pets = petServiceImpl.getBySpecieName(SPECIE_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findBySpecieName(SPECIE_NAME);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetBySpecieName_whenPetsNotFound_returnEmptyList() {
        // Arrange
        when(petRepositoryAdapter.findBySpecieName(SPECIE_NAME)).thenReturn(List.of());

        // Act
        List<PetOutDTO> pets = petServiceImpl.getBySpecieName(SPECIE_NAME);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findBySpecieName(SPECIE_NAME);
    }

    @Test
    public void testGetAll_whenPetsFound_returnPageOfPets() {
        List<Pet> petList = List.of(pet);
        Page<Pet> petPage = new PageImpl<>(petList, testPageable, petList.size());

        // Arrange
        when(petRepositoryAdapter.findAll(testPageable)).thenReturn(petPage);
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        Page<PetOutDTO> pets = petServiceImpl.getAll(testPageable);

        // Assert
        assertNotNull(pets);
        assertEquals(1, pets.getContent().size());

        // Verify
        verify(petRepositoryAdapter, times(1)).findAll(testPageable);
        verify(petMapper, times(1)).toOutDTO(pet);
    }

    @Test
    public void testGetAll_whenNoPetsFound_returnEmptyPage() {
        // Arrange
        when(petRepositoryAdapter.findAll(testPageable)).thenReturn(Page.empty());

        // Act
        Page<PetOutDTO> pets = petServiceImpl.getAll(testPageable);

        // Assert
        assertNotNull(pets);
        assertEquals(0, pets.getSize());

        // Verify
        verify(petRepositoryAdapter, times(1)).findAll(testPageable);
    }

    @Test
    public void testSave_whenValidData_returnSavedPet() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(specieRepositoryAdapter.findById(SPECIE_ID)).thenReturn(Optional.of(specie));
        when(breedRepositoryAdapter.findById(BREED_ID)).thenReturn(Optional.of(breed));
        when(petMapper.toEntity(petInDTO)).thenReturn(pet);
        when(petRepositoryAdapter.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.toOutDTO(pet)).thenReturn(petOutDTO);

        // Act
        PetOutDTO result = petServiceImpl.save(petInDTO, USERNAME);

        // Assert
        assertNotNull(result);
        assertEquals(petOutDTO.getName(), result.getName());

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(specieRepositoryAdapter, times(1)).findById(SPECIE_ID);
        verify(breedRepositoryAdapter, times(1)).findById(BREED_ID);
        verify(petRepositoryAdapter, times(1)).save(any(Pet.class));
    }

    @Test
    public void testSave_whenUserNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> petServiceImpl.save(petInDTO, USERNAME));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(specieRepositoryAdapter, times(0)).findByName(any());
        verify(breedRepositoryAdapter, times(0)).findByName(any());
        verify(petRepositoryAdapter, times(0)).save(any());
    }

    @Test
    public void testSave_whenSpecieNotFound_throwsIdNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(specieRepositoryAdapter.findById(SPECIE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> petServiceImpl.save(petInDTO, USERNAME));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(specieRepositoryAdapter, times(1)).findById(SPECIE_ID);
        verify(breedRepositoryAdapter, times(0)).findByName(any());
        verify(petRepositoryAdapter, times(0)).save(any());
    }

    @Test
    public void testSave_whenBreedNotFound_throwsIdNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(specieRepositoryAdapter.findById(SPECIE_ID)).thenReturn(Optional.of(specie));
        when(breedRepositoryAdapter.findById(BREED_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> petServiceImpl.save(petInDTO, USERNAME));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(specieRepositoryAdapter, times(1)).findById(SPECIE_ID);
        verify(breedRepositoryAdapter, times(1)).findById(BREED_ID);
        verify(petRepositoryAdapter, times(0)).save(any());
    }

    @Test
    public void testDelete_whenPetExists_deletePet() {
        // Arrange
        when(petRepositoryAdapter.findById(PET_ID)).thenReturn(Optional.of(pet));

        // Act
        petServiceImpl.delete(PET_ID);

        // Verify
        verify(petRepositoryAdapter, times(1)).findById(PET_ID);
        verify(petRepositoryAdapter, times(1)).delete(pet);
    }

    @Test
    public void testDelete_whenPetNotFound_throwsIdNotFoundException() {
        // Arrange
        when(petRepositoryAdapter.findById(PET_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> petServiceImpl.delete(PET_ID));

        // Verify
        verify(petRepositoryAdapter, times(1)).findById(PET_ID);
        verify(petRepositoryAdapter, times(0)).delete(any());
    }
}
