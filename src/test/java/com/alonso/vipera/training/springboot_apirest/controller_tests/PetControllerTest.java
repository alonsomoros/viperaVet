package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.config.TestConfig;
import com.alonso.vipera.training.springboot_apirest.controller.PetController;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetUpdateDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;
import com.alonso.vipera.training.springboot_apirest.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PetService petService;

    private Pageable testPageable;

    private User createdUser1;

    private Breed breed;

    private Specie specie;

    private Pet createdPet1;
    private Pet createdPet2;
    private PetInDTO petInDTO1;
    private PetOutDTO petOutDTO1;
    private PetOutDTO petOutDTO2;
    private PetUpdateDTO petUpdateDTO1;

    @BeforeEach
    void setUp() {

        testPageable = PageRequest.of(0, 10);

        createdUser1 = new User();
        createdUser1.setId(1L);
        createdUser1.setUsername("Alonso");

        breed = new Breed();
        breed.setId(54L);
        breed.setName("Border Collie");

        specie = new Specie();
        specie.setId(1l);
        specie.setName("Perro");

        specie.setBreeds(Set.of(breed));
        breed.setSpecie(specie);

        createdPet1 = new Pet();
        createdPet1.setId(1L);
        createdPet1.setName("Obi");
        createdPet1.setWeight(23.5d);
        createdPet1.setDietInfo("Comida para perros adultos");
        createdPet1.setBreed(breed);
        createdPet1.setSpecie(specie);
        createdPet1.setUser(createdUser1);
        createdPet1.setBirthDate(Date.valueOf("2025-11-09"));

        createdPet2 = new Pet();
        createdPet2.setId(2L);
        createdPet2.setName("Luna");
        createdPet2.setWeight(18.0d);
        createdPet2.setDietInfo("Comida para perros cachorros");
        createdPet2.setBreed(breed);
        createdPet2.setSpecie(specie);
        createdPet2.setUser(createdUser1);

        petInDTO1 = new PetInDTO();
        petInDTO1.setName(createdPet1.getName());
        petInDTO1.setWeight(createdPet1.getWeight());
        petInDTO1.setDietInfo(createdPet1.getDietInfo());
        petInDTO1.setBreedId(createdPet1.getBreed().getId());
        petInDTO1.setSpecieId(createdPet1.getSpecie().getId());
        petInDTO1.setBirthDate(Date.valueOf("2025-11-09"));

        petOutDTO1 = new PetOutDTO();
        petOutDTO1.setId(createdPet1.getId());
        petOutDTO1.setName(createdPet1.getName());
        petOutDTO1.setWeight(createdPet1.getWeight());

        petOutDTO2 = new PetOutDTO();
        petOutDTO2.setId(createdPet2.getId());
        petOutDTO2.setName(createdPet2.getName());
        petOutDTO2.setWeight(createdPet2.getWeight());

        petUpdateDTO1 = new PetUpdateDTO();
        petUpdateDTO1.setName(createdPet1.getName());
        petUpdateDTO1.setWeight(createdPet1.getWeight());
        petUpdateDTO1.setDietInfo(createdPet1.getDietInfo());
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetMyPets_whenValidResponse_shouldReturnOkAndPetList() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1, petOutDTO2);

        when(petService.getPetsByOwnerUsername("Alonso")).thenReturn(petOutDtoList);

        mockMvc.perform(get("/pets/my-pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(createdPet1.getId()))
                .andExpect(jsonPath("$[0].name").value(createdPet1.getName()))
                .andExpect(jsonPath("$[0].weight").value(createdPet1.getWeight()))
                .andExpect(jsonPath("$[1].id").value(createdPet2.getId()))
                .andExpect(jsonPath("$[1].name").value(createdPet2.getName()))
                .andExpect(jsonPath("$[1].weight").value(createdPet2.getWeight()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetWithFilters_whenIdFilterAndValidResponse_shouldReturnOkAndPetPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1, petOutDTO2);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());

        when(petService.getPetByFilters(petOutDTO1.getId(), null, null, null, testPageable)).thenReturn(petPage);

        mockMvc.perform(get("/pets?id=" + petOutDTO1.getId())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].id").value(createdPet1.getId()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetWithFilters_whenNameFilterAndValidResponse_shouldReturnOkAndPetPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());

        when(petService.getPetByFilters(null, petOutDTO1.getName(), null, null, testPageable)).thenReturn(petPage);

        mockMvc.perform(get("/pets?name=" + petOutDTO1.getName())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].name").value(createdPet1.getName()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetWithFilters_whenBreedIdFilterAndValidResponse_shouldReturnOkAndPetPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());
        Long breed_id = createdPet1.getBreed().getId();

        when(petService.getPetByFilters(null, null, breed_id, null, testPageable)).thenReturn(petPage);

        mockMvc.perform(get("/pets?breed_id=" + breed_id)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].id").value(createdPet1.getId()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetWithFilters_whenSpecieIdFilterAndValidResponse_shouldReturnOkAndPetPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());
        Long specie_id = createdPet1.getSpecie().getId();

        when(petService.getPetByFilters(null, null, null, specie_id, testPageable)).thenReturn(petPage);

        mockMvc.perform(get("/pets?specie_id=" + specie_id)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].id").value(createdPet1.getId()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetWithFilters_whenAllFiltersAndValidResponse_shouldReturnOkAndPetPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());

        Long breed_id = createdPet1.getBreed().getId();
        Long specie_id = createdPet1.getSpecie().getId();
        when(petService.getPetByFilters(petOutDTO1.getId(), petOutDTO1.getName(), breed_id, specie_id, testPageable))
                .thenReturn(petPage);

        mockMvc.perform(get("/pets?id=" + petOutDTO1.getId() + "&name=" + petOutDTO1.getName() + "&breed_id=" + breed_id
                + "&specie_id=" + specie_id)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].id").value(createdPet1.getId()))
                .andExpect(jsonPath("$.content[0].name").value(createdPet1.getName()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetAllPets_whenNoFiltersAndValidResponse_shouldReturnOkAndAllPetsPage() throws Exception {
        List<PetOutDTO> petOutDtoList = List.of(petOutDTO1);
        Page<PetOutDTO> petPage = new PageImpl<>(petOutDtoList, testPageable, petOutDtoList.size());

        when(petService.getAll(testPageable)).thenReturn(petPage);

        mockMvc.perform(get("/pets")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(petOutDtoList.size()))
                .andExpect(jsonPath("$.content[0].id").value(createdPet1.getId()))
                .andExpect(jsonPath("$.content[0].name").value(createdPet1.getName()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testRegisterPet_whenValidResponse_shouldReturnOkAndPet() throws Exception {

        when(petService.save(any(PetInDTO.class), eq("Alonso"))).thenReturn(petOutDTO1);

        mockMvc.perform(post("/pets")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petInDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdPet1.getId()))
                .andExpect(jsonPath("$.name").value(createdPet1.getName()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testDeletePet_whenValidResponse_shouldReturnNothing() throws Exception {

        doNothing().when(petService).delete(createdPet1.getId());

        mockMvc.perform(delete("/pets/" + createdPet1.getId())
                .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testUpdatePet_whenValidResponse_shouldReturnOkAndPet() throws Exception {

        when(petService.updatePet(createdPet1.getId(), petUpdateDTO1, createdPet1.getUser().getUsername()))
                .thenReturn(petOutDTO1);

        mockMvc.perform(patch("/pets/" + createdPet1.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petInDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdPet1.getId()))
                .andExpect(jsonPath("$.name").value(createdPet1.getName()));
    }

}
