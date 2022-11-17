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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
class ChooseTestControllerTest {
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
}