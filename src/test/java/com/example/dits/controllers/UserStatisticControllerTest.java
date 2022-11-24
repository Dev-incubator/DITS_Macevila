package com.example.dits.controllers;

import com.example.dits.dto.QuestionStatistic;
import com.example.dits.dto.TestStatistic;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.dto.UserStatistics;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.User;
import com.example.dits.mapper.StatisticMapper;
import com.example.dits.mapper.UserMapper;
import com.example.dits.service.StatisticService;
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
class UserStatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StatisticService statisticService;

    @MockBean
    StatisticMapper statisticMapper;

    @MockBean
    UserMapper userMapper;

    @Test
    void testUserStatistic() throws Exception {
        User user = new User();
        UserInfoDTO userInfoDTO = initializeUserInfoDTO();
        List<TestStatistic> testStatisticList = initializeTestStatisticList();

        UserStatistics userStatistics = UserStatistics.builder()
                .firstName("firstName")
                .lastName("lastName")
                .login("user")
                .testStatisticList(testStatisticList)
                .build();

        when(userMapper.convertUserInfoDTOToUser(userInfoDTO)).thenReturn(user);
        when(statisticService.getStatisticsByUser(user)).thenReturn(new ArrayList<Statistic>());
        when(statisticMapper.getUserStatistics(user, new ArrayList<Statistic>())).thenReturn(userStatistics);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/statistics")
                        .sessionAttr("user", userInfoDTO)
                        .sessionAttr("userStatistics", userStatistics))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private UserInfoDTO initializeUserInfoDTO() {
        return new UserInfoDTO(1, "firstName", "lastName", "user", "USER", "somePassword");
    }

    private List<TestStatistic> initializeTestStatisticList() {
        List<TestStatistic> testStatisticList = new ArrayList<>();

        testStatisticList.add(TestStatistic.builder()
                .testName("testName")
                .count(5)
                .avgProc(50)
                .questionStatistics(new ArrayList<QuestionStatistic>())
                .build());
        return testStatisticList;
    }
}