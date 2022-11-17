package com.example.dits.controllers;

import com.example.dits.dto.AnswerDTO;
import com.example.dits.dto.QuestionDTO;
import com.example.dits.dto.StatisticDTO;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.service.AnswerService;
import com.example.dits.service.QuestionService;
import com.example.dits.service.TestService;
import com.example.dits.service.UserService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
public class TestPageControllerTest {
    @MockBean
    UserService userService;
    @MockBean
    TestService testService;
    @MockBean
    AnswerService answerService;
    @MockBean
    QuestionService questionService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturn200WhenGoTestWithValidUri() throws Exception {
        int paramId = 1;
        com.example.dits.entity.Test test = new com.example.dits.entity.Test();
        List<QuestionDTO> questions = List.of(new QuestionDTO());
        List<AnswerDTO> answers = List.of(new AnswerDTO());
        String description = "Test";

        when(testService.getTestByTestId(paramId)).thenReturn(test);
        when(questionService.getQuestionsByTest(test)).thenReturn(questions);
        when(answerService.getAnswersFromQuestionList(questions, 0)).thenReturn(answers);
        when(questionService.getDescriptionFromQuestionList(questions, 0)).thenReturn(description);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/goTest")
                        .contentType(MediaType.TEXT_HTML)
                        .param("testId", "1")
                        .param("theme", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturn400WhenNotValidUri() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/goTest")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoMoreInteractions(testService);
        verifyNoMoreInteractions(questionService);
        verifyNoMoreInteractions(answerService);
    }

    @Test
    void shouldReturn200WhenResultPageWithValidUri() throws Exception {
        List<StatisticDTO> list = new ArrayList<>();
        list.add(new StatisticDTO());

        Map<String, Object> attrs = new HashMap<>();
        attrs.put("questions", List.of(new QuestionDTO()));
        attrs.put("questionNumber", 1);
        attrs.put("user", new UserInfoDTO());
        attrs.put("statistics", list);

        when(answerService.isRightAnswer(anyList(), anyList(), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/nextTestPage")
                        .contentType(MediaType.TEXT_HTML)
                        .param("answeredQuestion", "1")
                        .sessionAttrs(attrs))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
