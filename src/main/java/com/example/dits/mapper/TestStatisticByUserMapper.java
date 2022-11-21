package com.example.dits.mapper;
import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Test;
import com.example.dits.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestStatisticByUserMapper {
private final QuestionService questionService;

    @Autowired
    public TestStatisticByUserMapper(QuestionService questionService) {
        this.questionService = questionService;
    }

    public List<TestStatisticByUser> map(List<Statistic> statisticsOfUser) {
        List<TestStatisticByUser> testStatisticsByUser = new ArrayList<>();
        Map<String, List<Statistic>> map = getMapWithStatisticsByTestName(statisticsOfUser);

        map.forEach((testName, statisticList) -> {
            testStatisticsByUser.add(TestStatisticByUser.builder()
                    .testName(testName)
                    .count(statisticList.size() / questionService.getQuestionsByTestName(testName).size())
                    .avgProc(getAvgProcOfRightAnswers(statisticList))
                    .build());
        });

        return testStatisticsByUser;
    }

    private Map<String, List<Statistic>> getMapWithStatisticsByTestName(List<Statistic> statistics) {
        Map<String, List<Statistic>> map = new HashMap<>();

        statistics.forEach(stat -> {
            Test test = stat.getQuestion().getTest();
            map.computeIfAbsent(test.getName(), k -> new ArrayList<>()).add(stat);
        });

        return map;
    }

    private int getAvgProcOfRightAnswers(List<Statistic> statisticList) {
        int rightAnswers = 0;
        int avgProc = 0;

        for (Statistic statistic : statisticList) {
            if (statistic.isCorrect()) {
                rightAnswers++;
            }
        }

        avgProc = (int) Math.round((double) rightAnswers / statisticList.size() * 100);
        return avgProc;
    }
}

