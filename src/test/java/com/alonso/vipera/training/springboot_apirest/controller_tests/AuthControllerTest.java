package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
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
import com.alonso.vipera.training.springboot_apirest.controller.AuthController;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.AuthService;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthService authService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private User createdUser;
    private UserOutDTO userOutDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    public void setUp() {
        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("Alonso");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setEmail("alonso@gmail.com");
        registerRequestDTO.setPhone("666666666");
        registerRequestDTO.setAddress("Calle");
        registerRequestDTO.setRole(User.Role.OWNER);

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("Alonso");
        loginRequestDTO.setPassword("password");

        userOutDTO = new UserOutDTO();
        userOutDTO.setId(1L);
        userOutDTO.setUsername("Alonso");
        userOutDTO.setEmail("alonso@gmail.com");
        userOutDTO.setRole(User.Role.OWNER);

        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("mocked-jwt-token");
        authResponseDTO.setUser(userOutDTO);

    }

    @Test
    @WithMockUser(username = "Alonso")
    void testRegisterUser_shouldReturnOkAndAuthResponse() throws Exception {

        when(authService.register(registerRequestDTO)).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("Alonso"))
                .andExpect(jsonPath("$.user.email").value("alonso@gmail.com"))
                .andExpect(jsonPath("$.user.role").value("OWNER"));
    }

    @Test
    @WithMockUser(username = "Alonso")
    void testLoginUser_shouldReturnOkAndAuthResponse() throws Exception {

        when(authService.login(loginRequestDTO)).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("Alonso"))
                .andExpect(jsonPath("$.user.email").value("alonso@gmail.com"))
                .andExpect(jsonPath("$.user.role").value("OWNER"));
    }
}
