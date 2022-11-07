package com.example.dits.service;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Test;
import com.example.dits.entity.User;

import java.util.List;

public interface UserService {
    void update(UserInfoDTO userInfoDTO, int id);
    void delete(int id);
    void save(UserInfoDTO userInfoDTO);
    UserInfoDTO getUserInfoByLogin(String login);
    User getUserByLogin(String login);
    List<UserInfoDTO> getAllUsers();
}
