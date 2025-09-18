package com.example.daewoo.user.dto;

import lombok.Data;

@Data
public class EmailVerificationDto {
    private String userEmail;
    private String verificationCode;
}