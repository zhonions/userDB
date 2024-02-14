package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.exceptions.UserInvalidAttributesException;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;



    @Test
   void createUserSucess() throws Exception {
        User user = User.builder()
                .name("name")
                .password("Pass")
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUserWithNullNameReturnBadRequest() throws Exception {
        User user = User.builder()
                .name(null)
                .password("pass")
                .build();

        when(userService.createUser(user)).thenThrow(new UserInvalidAttributesException("Name and password cannot be null, empty or blank"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void createUserWithNullPasswordReturnBadRequest() throws Exception {
        User user = User.builder()
                .name("username")
                .password(null)
                .build();

        when(userService.createUser(user)).thenThrow(new UserInvalidAttributesException("Name and password cannot be null, empty or blank"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void getAllUsersReturnListOfUsers() throws Exception {
        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersReturnEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"))
                .andExpect(status().isOk());
    }
    @Test
    void getAllUsersReturnNotFoundStatus() throws Exception {
        when(userService.getAllUsers()).thenReturn(null);

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
     void findUserByIdReturnWhenIdExists() throws Exception {
        when(userService.findUserById(1L)).thenReturn(Optional.of(new User()));

        mockMvc.perform(get("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findUserByIdReturnNotFoundWhenUserNotExists() throws Exception {

        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void deleteUserByIdDeletesUserWhenUserExists() throws Exception {
        mockMvc.perform(delete("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
     void updateUserById() {
    }
}