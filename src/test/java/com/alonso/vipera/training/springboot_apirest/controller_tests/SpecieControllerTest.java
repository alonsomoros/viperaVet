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
import com.alonso.vipera.training.springboot_apirest.controller.SpecieController;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;
import com.alonso.vipera.training.springboot_apirest.service.SpecieService;

@WebMvcTest(SpecieController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class SpecieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SpecieService specieService;

    private SpecieOutDTO specie1;
    private SpecieOutDTO specie2;

    @BeforeEach
    public void setUp() {
        specie1 = new SpecieOutDTO();
        specie1.setId(1L);
        specie1.setName("Dog");

        specie2 = new SpecieOutDTO();
        specie2.setId(2L);
        specie2.setName("Cat");
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testGetAllSpecies_whenValidResponse_shouldReturnOkAndSpeciesList() throws Exception {
        List<SpecieOutDTO> specieList = List.of(specie1, specie2);
        when(specieService.getAll()).thenReturn(specieList);

        mockMvc.perform(get("/species"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(specieList.size()))
                .andExpect(jsonPath("$[0].id").value(specie1.getId()))
                .andExpect(jsonPath("$[0].name").value(specie1.getName()))
                .andExpect(jsonPath("$[1].id").value(specie2.getId()))
                .andExpect(jsonPath("$[1].name").value(specie2.getName()));

    }

}
