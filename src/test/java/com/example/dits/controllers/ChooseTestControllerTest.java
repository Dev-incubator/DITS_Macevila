package com.example.dits.controllers;

import com.example.dits.dto.TestInfoDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.service.TestService;
import com.example.dits.service.TopicService;
import org.junit.jupiter.api.Test;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
public class ChooseTestControllerTest {
    @MockBean
    TopicService topicService;
    @MockBean
    TestService testService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturn200WhenChooseTest() throws Exception {
        when(topicService.getTopicsWithQuestions()).thenReturn(List.of(new TopicDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/chooseTest").contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(topicService, times(1)).getTopicsWithQuestions();
        verifyNoMoreInteractions(topicService);
    }

    @Test
    void shouldReturnListOfTestInfoDTOWhenValidRequestParam() throws Exception {
        when(testService.getTestsByTopicName(anyString())).thenReturn(List.of(new TestInfoDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/chooseTheme")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("theme", anyString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(testService, times(1)).getTestsByTopicName(anyString());
        verifyNoMoreInteractions(testService);
    }

    @Test
    void shouldReturnEmptyListWhenEmptyRequestParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/chooseTheme").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verifyNoInteractions(testService);
    }
}
