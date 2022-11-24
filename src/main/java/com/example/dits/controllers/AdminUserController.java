package com.example.dits.controllers;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.mapper.UserMapper;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    UserService service;
    @Autowired
    RoleService roleService;
    @Autowired
    UserMapper mapper;
    @GetMapping("/userEditor")
    public String getUsers(ModelMap model) {
        model.addAttribute("userList", getUserList());
        model.addAttribute("title", "User editor");
        return "/admin/user-editor" ;
    }

    @DeleteMapping("/removeUser")
    public List<UserInfoDTO> removeUser(@RequestParam int userId) {
        service.delete(userId);
        return getUserList();
    }

    @ResponseBody
    @PutMapping("/editUser")
    public List<UserInfoDTO> editUser(@RequestBody UserInfoDTO userInfoDTO) {
        User user = null;
        int userId = userInfoDTO.getUserId();
        Role role = roleService.getRoleByRoleName(userInfoDTO.getRole());

        try {
            service.update(userInfoDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

       user  = mapper.convertUserInfoDTOToUser(userInfoDTO);
       user.setRole(role);

        return getUserList();
    }

    @ResponseBody
    @PostMapping("/addUser")
    public List<UserInfoDTO> addUser(@RequestBody UserInfoDTO userInfoDTO) {
        service.save(userInfoDTO);
        return getUserList();
    }

    @ResponseBody
    @GetMapping("/getUsers")
    public List<UserInfoDTO> getUserList() {
        return service.getAllUsers();
    }
}
