// src/main/java/com/example/daewoo/common/config/CustomAuthorizationRequestResolver.java

package com.example.daewoo.common.config;

// ... (기존 import 유지)
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;
    private static final String KAKAO_REGISTRATION_ID = "kakao";
    // 🚨 [추가] 구글과 네이버의 ID 정의
    private static final String GOOGLE_REGISTRATION_ID = "google";
    private static final String NAVER_REGISTRATION_ID = "naver";
    private static final String AUTHORIZATION_BASE_URI = "/oauth2/authorization/";

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, AUTHORIZATION_BASE_URI.substring(0, AUTHORIZATION_BASE_URI.length() - 1));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        // ... (기존 resolve 로직 유지)
        String requestUri = request.getRequestURI();
        String registrationId = null;
        // ... (registrationId 추출 로직 유지)
        if (requestUri.startsWith(AUTHORIZATION_BASE_URI)) {
            String path = requestUri.substring(AUTHORIZATION_BASE_URI.length());
            int indexOfSlash = path.indexOf('/');
            if (indexOfSlash > 0) {
                path = path.substring(0, indexOfSlash);
            }
            registrationId = path;
        }

        return this.resolve(request, registrationId);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

        // 1. 기본 Resolver를 사용하여 요청 객체 생성
        OAuth2AuthorizationRequest authorizationRequest =
                this.defaultResolver.resolve(request, clientRegistrationId);

        if (authorizationRequest != null) {
            Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

            // 🚨 [기존] 카카오 로그인 재인증 설정
            if (KAKAO_REGISTRATION_ID.equals(clientRegistrationId)) {
                additionalParameters.put("prompt", "login"); // 카카오
            }

            // 🚨 [추가] 구글 로그인 재인증 설정
            else if (GOOGLE_REGISTRATION_ID.equals(clientRegistrationId)) {
                // Google은 select_account를 사용하여 계정 선택 화면을 강제
                additionalParameters.put("prompt", "select_account");
            }

            // 🚨 [추가] 네이버 로그인 재인증 설정
            else if (NAVER_REGISTRATION_ID.equals(clientRegistrationId)) {
                // Naver는 auth_type=reprompt를 사용하여 재인증 요청
                additionalParameters.put("auth_type", "reprompt");
            }


            // 4. 새로운 요청 객체를 Builder로 생성하여 반환
            authorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(additionalParameters)
                    .build();
        }

        return authorizationRequest;
    }
}