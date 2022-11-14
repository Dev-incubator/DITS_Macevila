package com.example.dits.controllers;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserStatisticsController {

    private final StatisticService statisticService;
    private final UserService userService;

    @GetMapping("/statistics")
    public String statistics(ModelMap model, HttpSession session) {
        UserInfoDTO userDTO = (UserInfoDTO) session.getAttribute("user");
        User user = userService.getUserByLogin(userDTO.getLogin());
        List<TestStatisticByUser> testStatisticByUser = statisticService.getListOfUserTestStatisticsByUser(user);
        testStatisticByUser.sort(Comparator.comparingInt(TestStatisticByUser::getAvgProc).reversed());

        model.addAttribute("statistics", testStatisticByUser);
        return "user/personalStatistics";
    }
}