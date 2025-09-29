package com.example.daewoo.user.service;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

/**
 * OAuth2 서비스에서 받은 사용자 속성(Attributes)을 파싱하는 DTO
 */
@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String oauthId; // 소셜 서비스에서 받은 고유 ID
    private final String nickname;
    private final String userEmail; // 이메일 필드 추가 (DB 연동 시 중요)
    private final String profileImageUrl;
    private final String registrationId; // 서비스 이름 (kakao, naver, google)

    @Builder
    public OAuthAttributes(String registrationId, Map<String, Object> attributes, String nameAttributeKey, String oauthId, String nickname, String userEmail, String profileImageUrl) {
        this.registrationId = registrationId;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.profileImageUrl = profileImageUrl;
    }

    // 서비스 이름에 따라 정보를 추출하는 팩토리 메서드
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(registrationId, userNameAttributeName, attributes);
        } else if ("naver".equals(registrationId)) {
            return ofNaver(registrationId, userNameAttributeName, attributes);
        } else if ("google".equals(registrationId)) {
            return ofGoogle(registrationId, userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .oauthId((String) attributes.get("sub"))
                .nickname((String) attributes.get("name"))
                .userEmail((String) attributes.get("email")) // 🚨 Google은 'email' 필드가 최상위에 있음
                .profileImageUrl((String) attributes.get("picture"))
                .build();
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .oauthId((String) response.get("id"))
                .nickname((String) response.get("nickname"))
                .userEmail((String) response.get("email")) // 🚨 Naver는 'response' 안에 'email' 있음
                .profileImageUrl((String) response.get("profile_image"))
                .build();
    }

    // OAuthAttributes.java

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        // 카카오의 고유 ID (long 타입)는 'id' 속성으로 항상 존재합니다.
        String oauthId = String.valueOf(attributes.get(userNameAttributeName));

        // 닉네임/이미지는 'properties' 맵 안에 있습니다.
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        // 이메일 파싱 로직 (기존의 복잡한 로직을 제거하고, 임시 이메일 생성으로 대체)
        String email = oauthId + "@kakao.com"; // 🚨 카카오 고유 ID를 사용한 임시 이메일 생성

        // ***************************************************************
        // 🚨 닉네임과 프로필 이미지가 null인 경우를 대비한 Null 체크 추가
        String nickname = (properties != null) ? (String) properties.get("nickname") : "카카오 사용자";
        String profileImageUrl = (properties != null) ? (String) properties.get("profile_image") : null;
        // ***************************************************************


        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .oauthId(oauthId)
                .nickname(nickname)
                .userEmail(email) // 🚨 임시 이메일 사용
                .profileImageUrl(profileImageUrl)
                .build();
    }
}