package com.example.daewoo.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void createUser(UserDto user) {
        userMapper.insertUser(user);
    }

    public UserDto getUserById(int id) {
        return userMapper.findUserById(id);
    }

    public List<UserDto> getAllUser() {
        return userMapper.findAllUser();
    }

    public void updateUser(UserDto user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }
}