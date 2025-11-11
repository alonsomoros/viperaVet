package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.config.TestConfig;
import com.alonso.vipera.training.springboot_apirest.controller.BreedController;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.BreedService;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;

@WebMvcTest(BreedController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class BreedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BreedService breedService;

    private BreedOutDTO breed1;
    private BreedOutDTO breed2;

    @BeforeEach
    public void setUp() {
        breed1 = new BreedOutDTO();
        breed1.setId(1L);
        breed1.setName("Dog");

        breed2 = new BreedOutDTO();
        breed2.setId(2L);
        breed2.setName("Cat");
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetAllSpecies_whenValidResponse_shouldReturnOkAndSpeciesList() throws Exception {
        List<BreedOutDTO> breedList = List.of(breed1, breed2);
        when(breedService.getAllBreeds()).thenReturn(breedList);

        mockMvc.perform(get("/breeds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(breedList.size()))
                .andExpect(jsonPath("$[0].id").value(breed1.getId()))
                .andExpect(jsonPath("$[0].name").value(breed1.getName()))
                .andExpect(jsonPath("$[1].id").value(breed2.getId()))
                .andExpect(jsonPath("$[1].name").value(breed2.getName()));
    }

}
