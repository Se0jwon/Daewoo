package com.example.daewoo.user.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String userEmail;
    private String password;
}