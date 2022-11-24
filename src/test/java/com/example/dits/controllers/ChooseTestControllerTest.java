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
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

@ActiveProfiles("test")
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
public class ChooseTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TopicService topicService;

    @MockBean
    TestService testService;
    @Test
    void testUserPage() throws Exception {
        when(topicService.getTopicsWithQuestions()).thenReturn(new ArrayList<TopicDTO>());
        mockMvc.perform(get("/user/chooseTest"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetTestNameAndDescriptionFromTopic() throws Exception {
        when(testService.getTestsByTopicName("someTopic")).thenReturn(new ArrayList<TestInfoDTO>());
        mockMvc.perform(get("/user/chooseTheme"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200WhenChooseTest() throws Exception {
        when(topicService.getTopicsWithQuestions()).thenReturn(List.of(new TopicDTO()));

        mockMvc.perform(get("/user/chooseTest").contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        verify(topicService, times(1)).getTopicsWithQuestions();
        verifyNoMoreInteractions(topicService);
    }

    @Test
    void shouldReturnListOfTestInfoDTOWhenValidRequestParam() throws Exception {
        when(testService.getTestsByTopicName(anyString())).thenReturn(List.of(new TestInfoDTO()));

        mockMvc.perform(get("/user/chooseTheme")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("theme", anyString()))
                .andExpect(status().isOk());

        verify(testService, times(1)).getTestsByTopicName(anyString());
        verifyNoMoreInteractions(testService);
    }

    @Test
    void shouldReturnEmptyListWhenEmptyRequestParam() throws Exception {
        mockMvc.perform(get("/user/chooseTheme").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verifyNoInteractions(testService);
    }
}