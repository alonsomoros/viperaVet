package com.alonso.vipera.training.springboot_apirest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User createdUser1;
    private User createdUser2;
    private UserOutDTO userOutDTO1;
    private UserOutDTO userOutDTO2;

    @BeforeEach
    void setUp() {

        createdUser1 = new User();
        createdUser1.setId(1L);
        createdUser1.setUsername("Juan");
        createdUser1.setEmail("juan@gmail.com");
        createdUser1.setPassword("password123");

        createdUser2 = new User();
        createdUser2.setId(2L);
        createdUser2.setUsername("Cesar");
        createdUser2.setEmail("cesar@gmail.com");
        createdUser2.setPassword("password123");

        userOutDTO1 = new UserOutDTO();
        userOutDTO1.setId(1L);
        userOutDTO1.setUsername("Juan");
        userOutDTO1.setEmail("juan@gmail.com");

        userOutDTO2 = new UserOutDTO();
        userOutDTO2.setId(2L);
        userOutDTO2.setUsername("Cesar");
        userOutDTO2.setEmail("cesar@gmail.com");

    }

    @Test
    @WithMockUser
    void testGetAllUsers_whenValidResponse_shouldReturnOkAndUserList() throws Exception {
        List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1, userOutDTO2);

        when(userService.getAll()).thenReturn(usersOutDtoList);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(usersOutDtoList.size()))
                .andExpect(jsonPath("$[0].username").value(createdUser1.getUsername()))
                .andExpect(jsonPath("$[1].username").value(createdUser2.getUsername()));
    }

    @Test
    @WithMockUser
    void testGetUserById_whenIdExists_shouldReturnOkAndUser() throws Exception {
        when(userService.getById(createdUser1.getId())).thenReturn(userOutDTO1);

        mockMvc.perform(get("/users/{id}", createdUser1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser1.getId()))
                .andExpect(jsonPath("$.username").value(createdUser1.getUsername()))
                .andExpect(jsonPath("$.email").value(createdUser1.getEmail()));
    }

    // Mover a AuthControllerTest

    // @Test
    // void testPostUser_whenValidInput_shouldReturnCreated() throws Exception {
    // when(authService.register(userInDTO)).thenReturn(userOutDTO);

    // mockMvc.perform(post("/users/create")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectJSONMapper.writeValueAsString(userInDTO))
    // .accept(MediaType.APPLICATION_JSON))
    // .andDo(print())
    // .andExpect(status().isCreated())
    // .andExpect(jsonPath("$.id").value(userOutDTO.getId()))
    // .andExpect(jsonPath("$.username").value(userOutDTO.getUsername()))
    // .andExpect(jsonPath("$.email").value(userOutDTO.getEmail()));
    // }

}
