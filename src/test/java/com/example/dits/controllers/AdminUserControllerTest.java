package com.example.dits.controllers;

import com.example.dits.DAO.UserRepository;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
public class AdminUserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;


    @Test
    public void testAddUser() throws Exception {
        UserInfoDTO userInfoDTO = initializeUserDTO();
        mockMvc.perform(post("/admin/addUser")
                        .with(csrf())
                        .content(asJsonString(userInfoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditUser() throws Exception {
        User user = initializeUser();
        UserInfoDTO userInfoDTO = initializeUserDTO();
        when(userRepository.getById(1)).thenReturn(user);
        mockMvc.perform(put("/admin/editUser")
                        .with(csrf())
                        .content(asJsonString(userInfoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveUserById() throws Exception {
        mockMvc.perform(delete("/admin/removeUser?userId=1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserList() throws Exception {
        mockMvc.perform(get("/admin/getUsers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private User initializeUser() {
        return new User(1, "firstName", "lastName", "login", "password", new Role(), new ArrayList<>());
    }

    private UserInfoDTO initializeUserDTO() {
        return new UserInfoDTO(1, "firstname", "lastname", "login", "USER_ROLE", "password");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
