package com.alonso.vipera.training.springboot_apirest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.controller.UserRestController;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.model.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserRestController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectJSONMapper;

    @MockBean
    private UserService userService;

    private User createdUser1;

    private UserInDTO userInDTO;
    private UserOutDTO userOutDTO;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = new UserMapper();

        userInDTO = new UserInDTO();
        userInDTO.setUsername("Juan");
        userInDTO.setEmail("juan@gmail.com");
        userInDTO.setPassword("password123");

        createdUser1 = userMapper.toEntity(userInDTO);
        createdUser1.setId(1L);

        userOutDTO = userMapper.toOutDTO(createdUser1);

    }

    @Test
    void testGetAllUsers_whenValidResponse_shouldReturnOkAndUserList() throws Exception {
        User createdUser1 = new User(1L, "Juan", "juan@gmail.com", "password123", LocalDateTime.now());
        User createdUser2 = new User(2L, "Cesar", "cesar@gmail.com", "password123", LocalDateTime.now());
        List<User> createdUsersList = List.of(createdUser1, createdUser2);

        when(userService.getAll()).thenReturn(createdUsersList);

        mockMvc.perform(get("/users/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(createdUsersList.size()))
                .andExpect(jsonPath("$[0].username").value(createdUser1.getUsername()))
                .andExpect(jsonPath("$[1].username").value(createdUser2.getUsername()));
    }

    @Test
    void testGetUserById_whenIdExists_shouldReturnOkAndUser() throws Exception {
        User createdUser1 = new User(1L, "Juan", "juan@gmail.com", "password123", LocalDateTime.now());
        when(userService.getById(createdUser1.getId())).thenReturn(createdUser1);

        mockMvc.perform(get("/users/{id}", createdUser1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser1.getId()))
                .andExpect(jsonPath("$.username").value(createdUser1.getUsername()))
                .andExpect(jsonPath("$.email").value(createdUser1.getEmail()));
    }

    @Test
    void testGetUserByUsername_whenUsernameExists_shouldReturnOkAndUser() throws Exception {
        when(userService.getByUsername(createdUser1.getUsername())).thenReturn(createdUser1);

        mockMvc.perform(get("/users/by-username")
                .param("username", createdUser1.getUsername())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser1.getId()))
                .andExpect(jsonPath("$.username").value(createdUser1.getUsername()))
                .andExpect(jsonPath("$.email").value(createdUser1.getEmail()));
    }

    @Test
    void testPostUser_whenValidInput_shouldReturnCreated() throws Exception {
        when(userService.create(userInDTO)).thenReturn(userOutDTO);

        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectJSONMapper.writeValueAsString(userInDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userOutDTO.getId()))
                .andExpect(jsonPath("$.username").value(userOutDTO.getUsername()))
                .andExpect(jsonPath("$.email").value(userOutDTO.getEmail()));
    }

}
