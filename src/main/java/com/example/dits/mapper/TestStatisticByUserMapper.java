package com.example.dits.mapper;
import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Test;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestStatisticByUserMapper {
    public List<TestStatisticByUser> map(List<Statistic> statistics) {
        List<TestStatisticByUser> testStatisticsByUser = new ArrayList<>();
        Map<String, List<Statistic>> map = getMapWithStatisticsByTestName(statistics);

        map.forEach((testName, statisticList) -> {
            testStatisticsByUser.add(TestStatisticByUser.builder()
                    .testName(testName)
                    .count(statisticList.size())
                    .avgProc(getAvgProcOfRightAnswers(statisticList))
                    .build());
        });

        return testStatisticsByUser;
    }

    private Map<String, List<Statistic>> getMapWithStatisticsByTestName(List<Statistic> statistics) {
        Map<String, List<Statistic>> map = new HashMap<>();

        for (Statistic statistic : statistics) {
            Test test = statistic.getQuestion().getTest();
            List<Statistic> list = map.get(test.getName());

            if (list != null) {
                list.add(statistic);
            } else {
                List<Statistic> statisticList = new ArrayList<>();
                statisticList.add(statistic);
                map.put(test.getName(), statisticList);
            }
        }

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

