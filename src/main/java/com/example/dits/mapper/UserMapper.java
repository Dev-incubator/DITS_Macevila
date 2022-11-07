package com.example.dits.mapper;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {
    @Autowired
    UserService service;

    public User convertUserInfoDTOToUser(UserInfoDTO userInfoDTO) {
        return service.getUserByLogin(userInfoDTO.getLogin());
    }
}
