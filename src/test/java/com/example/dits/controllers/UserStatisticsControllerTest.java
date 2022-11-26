package com.example.dits.controllers;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import com.example.dits.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
public class UserStatisticsControllerTest {
    @MockBean
    UserService userService;
    @MockBean
    StatisticService statisticService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturn200WhenCheckStatistics() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setLogin("Test");

        Mockito.when(userService.getUserByLogin(anyString())).thenReturn(mock(User.class));
        Mockito.when(statisticService.getListOfUserTestStatisticsByUser(mock(User.class))).thenReturn(anyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/user/statistics").contentType(MediaType.TEXT_HTML).sessionAttr("user", userInfoDTO))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
