package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.config.TestConfig;
import com.alonso.vipera.training.springboot_apirest.controller.PruebaController;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.BreedService;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;
import com.alonso.vipera.training.springboot_apirest.service.SpecieService;

@WebMvcTest(PruebaController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class PruebaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BreedService breedService;

    @MockBean
    private SpecieService specieService;

    @Test
    @WithMockUser(username = "Alonso")
    void testGetBreedByName_shouldReturnOkAndBreed() throws Exception {
        String breedName = "Border Collie";
        BreedOutDTO breed = new BreedOutDTO(41L, breedName, "50", null);

        when(breedService.findByName(breedName)).thenReturn(breed);

        mockMvc.perform(get("/prueba/getBreed")
                .param("breedName", breedName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(breedName));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetSpecieByName_shouldReturnOkAndSpecie() throws Exception {
        String specieName = "Perro";
        SpecieOutDTO specie = new SpecieOutDTO(1L, specieName);

        when(specieService.findByName(specieName)).thenReturn(specie);

        mockMvc.perform(get("/prueba/getSpecie")
                .with(csrf())
                .param("specieName", specieName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(specieName));
    }
}
