package com.example.dits.controllers;

import com.example.dits.DAO.StatisticRepository;
import com.example.dits.entity.Topic;
import com.example.dits.service.TopicService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
public class AdminStatisticControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    StatisticRepository statisticRepository;
    @MockBean
    TopicService topicService;

    @Test
    public void testGetTestStatisticByTopicId() throws Exception {
        Topic topic = initializeTopic();
        when(topicService.getTopicByTopicId(1)).thenReturn(topic);
        mockMvc.perform(get("/admin/getTestsStatistic?id=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveStatisticById() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/byId?id=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveAllStatistic() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    private Topic initializeTopic() {
        return new Topic(1, "topic", "top", new ArrayList<>());
    }
}
