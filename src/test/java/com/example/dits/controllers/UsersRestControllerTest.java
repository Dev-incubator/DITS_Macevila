package com.example.dits.controllers;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import com.example.dits.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    StatisticService statisticService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn200WhenWeGetUsers() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldMapsToBusinessModelWhenValidInput() throws Exception {
        UserInfoDTO expected = new UserInfoDTO();
        expected.setUserId(1);
        expected.setFirstName("Test");

        when(userService.getUserInfoById(1)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).getUserInfoById(1);
        UserInfoDTO resultUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserInfoDTO.class);
        Assertions.assertEquals(resultUser, expected);
    }

    @Test
    void shouldMapsToBusinessModelWhenNotValidInput() throws Exception {
        when(userService.getUserInfoById(anyInt())).thenReturn(new UserInfoDTO());

        mockMvc.perform(get("/users/{id}", anyInt()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnListOfStatisticsWhenValidInput() throws Exception {
        User user = new User();
        TestStatisticByUser statistic = new TestStatisticByUser();
        statistic.setTestName("Test");
        statistic.setCount(1);
        List<TestStatisticByUser> statisticByUserList = List.of(statistic);

        when(userService.getUserById(1)).thenReturn(user);
        when(statisticService.getListOfUserTestStatisticsByUser(user)).thenReturn(statisticByUserList);

        mockMvc.perform(get("/users/{id}/statistics", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].testName", Matchers.is("Test")));

        verify(userService,times(1)).getUserById(1);
        verify(statisticService, times(1)).getListOfUserTestStatisticsByUser(user);
    }
}
