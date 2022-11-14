package com.example.dits.controllers;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersRestController {
    private final UserService userService;
    private final StatisticService statisticService;

    @GetMapping("/{id}")
    public UserInfoDTO getUser(@PathVariable("id") Integer id) {
        return userService.getUserInfoById(id);
    }

    @GetMapping("/{id}/statistics")
    public List<TestStatisticByUser> getUserStatistics(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        return statisticService.getListOfUserTestStatisticsByUser(user);
    }
}
