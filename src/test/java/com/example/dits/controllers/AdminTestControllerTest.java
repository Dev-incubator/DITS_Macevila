package com.example.dits.controllers;

import com.example.dits.DAO.QuestionRepository;
import com.example.dits.DAO.TestRepository;
import com.example.dits.DAO.TopicRepository;
import com.example.dits.dto.QuestionEditModel;
import com.example.dits.dto.TestInfoDTO;
import com.example.dits.dto.TopicDTO;
import com.example.dits.entity.Topic;
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
public class AdminTestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TopicRepository topicRepository;
    @MockBean
    TestRepository testRepository;
    @MockBean
    QuestionRepository questionRepository;

    @Test
    public void testAddTopic() throws Exception {
        TopicDTO topic = initializeTopicDTO();
        mockMvc.perform(post("/admin/addTopic")
                        .with(csrf())
                        .content(asJsonString(topic))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditTopicById() throws Exception {
        Topic topic = initializeTopic();
        TopicDTO topicDTO = initializeTopicDTO();
        when(topicRepository.getTopicByTopicId(topicDTO.getTopicId())).thenReturn(topic);
        mockMvc.perform(put("/admin/editTopic")
                        .with(csrf())
                        .content(asJsonString(topicDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveTopicById() throws Exception {
        mockMvc.perform(delete("/admin/removeTopic?topicId=1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTopicList() throws Exception {
        mockMvc.perform(get("/admin/getTopics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddTest() throws Exception {
        Topic topic = initializeTopic();
        when(topicRepository.getTopicByTopicId(1)).thenReturn(topic);
        TestInfoDTO test = initializeTestDTO();
        mockMvc.perform(post("/admin/addTest")
                        .with(csrf())
                        .content(asJsonString(test))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditTestById() throws Exception {
        TestInfoDTO testInfoDTO = initializeTestDTO();
        com.example.dits.entity.Test test = initializeTest();
        Topic topic = initializeTopic();
        when(topicRepository.getTopicByTopicId(1)).thenReturn(topic);
        when(testRepository.getTestByTestId(1)).thenReturn(test);
        mockMvc.perform(put("/admin/editTest")
                        .with(csrf())
                        .content(asJsonString(testInfoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveTestById() throws Exception {
        com.example.dits.entity.Test test = initializeTest();
        Topic topic = initializeTopic();
        when(topicRepository.getTopicByTopicId(1)).thenReturn(topic);
        when(testRepository.getTestByTestId(1)).thenReturn(test);
        mockMvc.perform(delete("/admin/removeTest?testId=1&topicId=1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddQuestion() throws Exception {
        com.example.dits.entity.Test test = initializeTest();
        Topic topic = initializeTopic();
        when(topicRepository.getTopicByTopicId(1)).thenReturn(topic);
        when(testRepository.getTestByTestId(1)).thenReturn(test);
        QuestionEditModel question = initializeQuestionDTO();
        mockMvc.perform(post("/admin/addQuestion")
                        .with(csrf())
                        .content(asJsonString(question))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveQuestionById() throws Exception {
        Topic topic = initializeTopic();
        when(topicRepository.getTopicByTopicId(1)).thenReturn(topic);
        mockMvc.perform(delete("/admin/removeQuestion?questionId=1&topicId=1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private TopicDTO initializeTopicDTO() {
        return new TopicDTO("topic", 1);
    }

    private Topic initializeTopic() {
        return new Topic(1, "topic", "top", new ArrayList<>());
    }

    private TestInfoDTO initializeTestDTO() {
        return new TestInfoDTO(1, "test", "description", 1);
    }

    private com.example.dits.entity.Test initializeTest() {
        return new com.example.dits.entity.Test(1, "description", "name", initializeTopic(), new ArrayList<>());
    }

    private QuestionEditModel initializeQuestionDTO() {
        return new QuestionEditModel("name", 1, 1, 1, new ArrayList<>());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
