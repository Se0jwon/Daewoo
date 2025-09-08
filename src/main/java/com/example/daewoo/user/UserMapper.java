package com.example.daewoo.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
//    C
    void insertUser(UserDto user);
//    R
    UserDto findUserById(int id);
    List<UserDto> findAllUser();
//    U
    void updateUser(UserDto user);
//    D
    void deleteUser(int id);
}
