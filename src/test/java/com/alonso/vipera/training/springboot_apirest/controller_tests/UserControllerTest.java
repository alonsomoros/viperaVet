package com.alonso.vipera.training.springboot_apirest.controller_tests;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.alonso.vipera.training.springboot_apirest.config.TestConfig;
import com.alonso.vipera.training.springboot_apirest.controller.UserRestController;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.UserUpdateDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;
import com.alonso.vipera.training.springboot_apirest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserRestController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtService jwtService;

        private Pageable testPageable;

        private User createdUser1;
        private User createdUser2;
        private UserOutDTO userOutDTO1;
        private UserOutDTO userOutDTO2;
        private UserUpdateDTO userUpdateDTO;
        private UserOutDTO userUpdatedOutDTO;

        @BeforeEach
        void setUp() {
                testPageable = PageRequest.of(0, 10);

                createdUser1 = new User();
                createdUser1.setId(1L);
                createdUser1.setUsername("Juan");
                createdUser1.setEmail("juan@gmail.com");
                createdUser1.setPassword("password123");
                createdUser1.setRole(User.Role.OWNER);

                createdUser2 = new User();
                createdUser2.setId(2L);
                createdUser2.setUsername("Cesar");
                createdUser2.setEmail("cesar@gmail.com");
                createdUser2.setPassword("password123");

                userOutDTO1 = new UserOutDTO();
                userOutDTO1.setId(1L);
                userOutDTO1.setUsername("Juan");
                userOutDTO1.setEmail("juan@gmail.com");
                userOutDTO1.setRole(User.Role.OWNER);

                userOutDTO2 = new UserOutDTO();
                userOutDTO2.setId(2L);
                userOutDTO2.setUsername("Cesar");
                userOutDTO2.setEmail("cesar@gmail.com");

                userUpdateDTO = new UserUpdateDTO();
                userUpdateDTO.setPhone("666555444");
                userUpdateDTO.setEmail("updated@gmail.com");
                userUpdateDTO.setAddress("Updated Address");

                userUpdatedOutDTO = new UserOutDTO();
                userUpdatedOutDTO.setId(1L);
                userUpdatedOutDTO.setUsername("Juan");
                userUpdatedOutDTO.setEmail("updated@gmail.com");

        }

        @Test
        @WithMockUser
        void testGetAllUsers_whenValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1, userOutDTO2);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getAll(testPageable)).thenReturn(userPage);

                mockMvc.perform(get("/users")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].username").value(createdUser1.getUsername()))
                                .andExpect(jsonPath("$.content[1].username").value(createdUser2.getUsername()));
        }

        @Test
        @WithMockUser
        void testGetUsersWithFilters_whenIdFilterAndValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getUserByFilters(userOutDTO1.getId(), null, null, null, testPageable))
                                .thenReturn(userPage);

                mockMvc.perform(get("/users?id=" + userOutDTO1.getId())
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].id").value(createdUser1.getId()));
        }

        @Test
        @WithMockUser
        void testGetUsersWithFilters_whenUsernameFilterAndValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getUserByFilters(null, userOutDTO1.getUsername(), null, null, testPageable))
                                .thenReturn(userPage);

                mockMvc.perform(get("/users?username=" + userOutDTO1.getUsername())
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].username").value(createdUser1.getUsername()));
        }

        @Test
        @WithMockUser
        void testGetUsersWithFilters_whenEmailFilterAndValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getUserByFilters(null, null, userOutDTO1.getEmail(), null, testPageable))
                                .thenReturn(userPage);

                mockMvc.perform(get("/users?email=" + userOutDTO1.getEmail())
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].email").value(createdUser1.getEmail()));
        }

        @Test
        @WithMockUser
        void testGetUsersWithFilters_whenRoleFilterAndValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getUserByFilters(null, null, null, userOutDTO1.getRole(), testPageable))
                                .thenReturn(userPage);

                mockMvc.perform(get("/users?role=" + userOutDTO1.getRole())
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].role").value(createdUser1.getRole().name()));
        }

        @Test
        @WithMockUser
        void testGetUsersWithFilters_whenAllFiltersAndValidResponse_shouldReturnOkAndUserPage() throws Exception {
                List<UserOutDTO> usersOutDtoList = List.of(userOutDTO1);
                Page<UserOutDTO> userPage = new PageImpl<>(usersOutDtoList, testPageable, usersOutDtoList.size());

                when(userService.getUserByFilters(userOutDTO1.getId(), userOutDTO1.getUsername(),
                                userOutDTO1.getEmail(),
                                userOutDTO1.getRole(), testPageable))
                                .thenReturn(userPage);

                mockMvc.perform(get("/users?id=" + userOutDTO1.getId() + "&username=" + userOutDTO1.getUsername()
                                + "&email="
                                + userOutDTO1.getEmail() + "&role=" + userOutDTO1.getRole())
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(usersOutDtoList.size()))
                                .andExpect(jsonPath("$.content[0].id").value(createdUser1.getId()))
                                .andExpect(jsonPath("$.content[0].username").value(createdUser1.getUsername()))
                                .andExpect(jsonPath("$.content[0].email").value(createdUser1.getEmail()))
                                .andExpect(jsonPath("$.content[0].role").value(createdUser1.getRole().name()));
        }

        @Test
        @WithMockUser
        void testDeleteUser_whenValidResponse_shouldReturnVoid() throws Exception {

                doNothing().when(userService).delete(createdUser1.getId());

                mockMvc.perform(delete("/users/{id}", createdUser1.getId())
                                .with(csrf()))
                                .andExpect(status().isNoContent())
                                .andExpect(content().string(""));
        }

        @Test
        @WithMockUser(username = "Juan")
        void testUpdateUser_whenValidResponse_shouldReturnOkAndUser() throws Exception {
                when(userService.updateUser(createdUser1.getId(), userUpdateDTO, createdUser1.getUsername()))
                                .thenReturn(userUpdatedOutDTO);

                mockMvc.perform(patch("/users/{id}", createdUser1.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userUpdateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(createdUser1.getId()))
                                .andExpect(jsonPath("$.username").value(createdUser1.getUsername()))
                                .andExpect(jsonPath("$.email").value(userUpdatedOutDTO.getEmail()));
        }

}
