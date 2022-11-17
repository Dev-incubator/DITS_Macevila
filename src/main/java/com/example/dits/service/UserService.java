package com.example.dits.service;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;

import java.util.List;

public interface UserService {
    UserInfoDTO getUserInfoByLogin(String login);
    UserInfoDTO getUserInfoById(Integer id);
    User getUserByLogin(String login);
    User getUserById(Integer id);
    List<UserInfoDTO> getAllUsers();

    void save(User user);

    void update(User user, Integer id);

    void delete(User user);

    List<User> findAll();
}
