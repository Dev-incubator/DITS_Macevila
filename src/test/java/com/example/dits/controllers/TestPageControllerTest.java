package com.example.dits.controllers;

import com.example.dits.dto.AnswerDTO;
import com.example.dits.dto.QuestionDTO;
import com.example.dits.dto.StatisticDTO;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.service.AnswerService;
import com.example.dits.service.QuestionService;
import com.example.dits.service.StatisticService;
import com.example.dits.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", authorities = {"ROLE_USER"})
class TestPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TestService testService;

    @MockBean
    QuestionService questionService;

    @MockBean
    AnswerService answerService;

    @MockBean
    StatisticService statisticService;

    @Test
    void testGoTest() throws Exception {
        int testId = 1;
        int questionNumber = 0;
        String questionDescription = "some description";
        var test = new com.example.dits.entity.Test();

        List<QuestionDTO> questionList = new ArrayList<>();
        List<AnswerDTO> answers = new ArrayList<>();

        when(testService.getTestByTestId(testId)).thenReturn(test);
        when(questionService.getQuestionsByTest(test)).thenReturn(questionList);
        when(answerService.getAnswersFromQuestionList(questionList, questionNumber)).thenReturn(answers);
        when(questionService.getDescriptionFromQuestionList(questionList, questionNumber)).thenReturn(questionDescription);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/goTest")
                        .param("testId", String.valueOf(testId))
                        .param("theme", "some theme"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testNextTestPage() throws Exception {
        int questionNumber = 1;
        String questionDescription = "some description";
        boolean isCorrect = true;

        List<QuestionDTO> questionList = initializeQuestionDTOList();
        List<AnswerDTO> answers = new ArrayList<>();
        List<Integer> answeredQuestion = new ArrayList<>();
        UserInfoDTO user = initializeUserInfoDTO();
        List<StatisticDTO> statisticList = new ArrayList<>();
        statisticList.add(StatisticDTO.builder()
                .questionId(questionList.get(questionNumber - 1).getQuestionId())
                .username(user.getLogin())
                .isCorrect(isCorrect)
                .build());

        when(answerService.isRightAnswer(answeredQuestion, questionList, questionNumber)).thenReturn(isCorrect);
        when(answerService.getAnswersFromQuestionList(questionList, questionNumber)).thenReturn(answers);
        when(questionService.getDescriptionFromQuestionList(questionList, questionNumber)).thenReturn(questionDescription);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/nextTestPage")
                        .sessionAttr("questions", questionList)
                        .sessionAttr("questionNumber", questionNumber)
                        .sessionAttr("user", user)
                        .sessionAttr("statistics", statisticList))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testTestStatistic() throws Exception {
        String topicName = "topicName";
        String testName = "testName";
        int percentageOfCorrect = 100;
        int quantityOfRightAnswers = 10;
        int questionSize = 12;
        int questionNumber = 1;
        boolean isCorrect = true;

        List<Integer> answeredQuestion = new ArrayList<>();
        List<QuestionDTO> questionList = initializeQuestionDTOList();
        UserInfoDTO user = initializeUserInfoDTO();
        List<StatisticDTO> statisticList = new ArrayList<>();
        statisticList.add(StatisticDTO.builder()
                .questionId(questionList.get(questionNumber - 1).getQuestionId())
                .username(user.getLogin())
                .isCorrect(isCorrect)
                .build());

        when(answerService.isRightAnswer(answeredQuestion, questionList, questionNumber)).thenReturn(isCorrect);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/resultPage")
                        .sessionAttr("topicName", topicName)
                        .sessionAttr("testName", testName)
                        .sessionAttr("percentageOfCorrect", percentageOfCorrect)
                        .sessionAttr("quantityOfRightAnswers", quantityOfRightAnswers)
                        .sessionAttr("questionSize", questionSize)
                        .sessionAttr("questions", questionList)
                        .sessionAttr("user", user)
                        .sessionAttr("statistics", statisticList))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private UserInfoDTO initializeUserInfoDTO() {
        return new UserInfoDTO(1, "firstName", "lastName", "user", "USER", "somePassword");
    }

    private List<QuestionDTO> initializeQuestionDTOList() {
        List<QuestionDTO> questionList = new ArrayList<>();
        questionList.add(new QuestionDTO(1, "some description"));

        return questionList;
    }
}