package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.config.TestConfig;
import com.alonso.vipera.training.springboot_apirest.controller.DogApiBreedsController;
import com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in.DogApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.DogApiBreedsService;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;

@WebMvcTest(DogApiBreedsController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class DogApiBreedsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private DogApiBreedsService dogApiBreedsService;

    @Test
    @WithMockUser(username = "Alonso")
    void testGetAllBreeds_shouldReturnOkAndBreedsList() throws Exception {
        List<DogApiBreedInDTO> breedsList = List.of(new DogApiBreedInDTO(1, "Bulldog", "Desc"));

        when(dogApiBreedsService.getAllDogBreeds()).thenReturn(breedsList);

        mockMvc.perform(get("/api/dog-breeds")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(breedsList.get(0).getName()));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testSaveAllBreeds_shouldReturnOkAndSavedBreedsList() throws Exception {
        List<BreedOutDTO> breedsList = List.of(new BreedOutDTO(1L, "Bulldog", null, null));

        when(dogApiBreedsService.saveAllDogsBreeds()).thenReturn(breedsList);

        mockMvc.perform(post("/api/dog-breeds/save-all")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bulldog"));
    }

}
