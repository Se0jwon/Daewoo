package com.example.daewoo.user.service;

import com.example.daewoo.user.dto.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    // 🚨 [필수] PasswordEncoder 주입
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 1. 소셜별 속성 파싱
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 2. DB에서 사용자 정보 확인
        Optional<UserEntity> userOptional = userRepository.findByUserEmail(attributes.getUserEmail());
        UserEntity user;

        if (userOptional.isPresent()) {
            // 3. 기존 사용자: 정보 업데이트 & DB 저장 (로그인)
            user = userOptional.get();
            user.setUsername(attributes.getNickname());
            user.setImageUrl(attributes.getProfileImageUrl());

            user.setRegistrationId(attributes.getRegistrationId());
            user.setOauthId(attributes.getOauthId());

        } else {
            // 4. 🚨 신규 사용자: 임시 사용자 정보 생성 및 DB 저장 (Pending Registration)

            LocalDate tempBirth = LocalDate.of(1900, 1, 1);

            user = UserEntity.builder()
                    .userEmail(attributes.getUserEmail())
                    .username(attributes.getNickname())
                    .imageUrl(attributes.getProfileImageUrl())

                    // 🚨 [필수] SocialSignUp 페이지로 리다이렉트하기 위한 권한 설정
                    .role("ROLE_PENDING_REGISTRATION")

                    // 🚨 [필수] 소셜 정보를 UserEntity에 저장
                    .registrationId(attributes.getRegistrationId())
                    .oauthId(attributes.getOauthId())

                    // 🚨 [핵심 수정] 임시 비밀번호도 반드시 암호화하여 저장
                    .password(passwordEncoder.encode("TEMP_OAUTH_PASSWORD"))

                    // 🚨 UserEntity의 NOT NULL 제약조건을 피하기 위한 임시 값
                    .userAddress("TEMP_ADDR")
                    .userPhone("000-0000-0000")
                    .userBirth(tempBirth)
                    .build();
        }

        // 5. DB에 저장 후 반환
        userRepository.save(user);

        // 6. OAuth2SuccessHandler로 전달할 추가 속성 설정 (UserEntity에 저장된 role 포함)
        Map<String, Object> additionalAttributes = new HashMap<>(oAuth2User.getAttributes());
        additionalAttributes.put("oauthId", user.getOauthId());
        additionalAttributes.put("registrationId", user.getRegistrationId());
        additionalAttributes.put("userEmail", user.getUserEmail());
        additionalAttributes.put("nickname", user.getUsername());
        additionalAttributes.put("userId", user.getUserId());

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));

        return new DefaultOAuth2User(authorities, additionalAttributes, userNameAttributeName);
    }
}