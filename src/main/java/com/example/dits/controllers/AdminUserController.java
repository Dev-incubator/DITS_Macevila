package com.example.dits.controllers;

import com.example.dits.dto.UserInfoDTO;
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
    @GetMapping("/userEditor")
    public String getUsers(ModelMap model) {
        model.addAttribute("userList", getUserList());
        model.addAttribute("title", "User editor");
        return "admin/user-list";
    }

    @DeleteMapping("/deleteUser")
    public List<UserInfoDTO> deleteUser(@RequestParam int id) {
        service.delete(id);
        return getUserList();
    }

    @ResponseBody
    @PostMapping("/editUser")
    public List<UserInfoDTO> editUser(@RequestBody UserInfoDTO userInfoDTO) {
        service.update(userInfoDTO, userInfoDTO.getUserId());
        return getUserList();
    }

    public List<UserInfoDTO> createUser(@RequestBody UserInfoDTO userInfoDTO) {
        service.save(userInfoDTO);
        return getUserList();
    }

    @ResponseBody
    @GetMapping("/getUsers")
    public List<UserInfoDTO> getUserList() {
        return service.getAllUsers();
    }
}
