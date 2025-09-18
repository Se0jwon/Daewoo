package com.example.daewoo.user.dto;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String userEmail;
    private String verificationCode;
    private String newPassword;
}