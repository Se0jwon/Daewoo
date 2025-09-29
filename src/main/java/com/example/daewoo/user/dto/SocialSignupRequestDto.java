package com.example.daewoo.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class SocialSignupRequestDto {
    // CustomOAuth2UserService에서 받은 정보 (숨겨진 필드)
    private String oauthId;
    private String registrationId;

    // 사용자 추가 입력 정보
    private String userEmail;

    private String username;

    private String userAddress;
    private String userPhone;
    private LocalDate userBirth;
}