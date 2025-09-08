package com.example.daewoo.user;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String userName;
    private String userPwd;
    private String userAddress;
    private String userPhone;
    private String userEmail;
    private String userBirth;
}