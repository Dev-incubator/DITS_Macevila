package com.example.dits.service.impl;

import com.example.dits.DAO.UserRepository;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Transactional
    @Override
    public void update(UserInfoDTO userInfoDTO, int id) {
        User userEdited = repository.getById(id);
        Role userRole = roleService.getRoleByRoleName(userInfoDTO.getRole());
        userEdited.setFirstName(userInfoDTO.getFirstName());
        userEdited.setLastName(userInfoDTO.getLastName());
        userEdited.setRole(userRole);
        userEdited.setLogin(userInfoDTO.getLogin());
        userEdited.setPassword(encoder.encode(userInfoDTO.getPassword()));
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void save(UserInfoDTO userInfoDTO) {
        Role role = roleService.getRoleByRoleName(userInfoDTO.getRole());
        User user = User.builder()
                .firstName(userInfoDTO.getFirstName())
                .lastName(userInfoDTO.getLastName())
                .role(role)
                .login(userInfoDTO.getLogin())
                .password(encoder.encode(userInfoDTO.getPassword()))
                .build();
        repository.save(user);
    }

    @Transactional
    public UserInfoDTO getUserInfoByLogin(String login){
        return modelMapper.map(repository.getUserByLogin(login), UserInfoDTO.class);
    }

    @Transactional
    public UserInfoDTO getUserInfoById(Integer id) {
        User user = repository.getUserByUserId(id);

        if (user != null) {
            return modelMapper.map(user, UserInfoDTO.class);
        }

        return new UserInfoDTO();
    }

    @Override
    public User getUserByLogin(String login) {
        return repository.getUserByLogin(login);
    }

    @Override
    public User getUserById(Integer id) {
        return repository.getUserByUserId(id);
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        return repository.findAll().stream().map(f -> modelMapper.map(f, UserInfoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public void update(User user, Integer id) {
        if (repository.findById(id).isPresent()) {
            repository.save(user);
        }
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }
}
