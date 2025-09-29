// CustomAuthorizationRequestResolver.java (URL 파싱으로 최종 수정)

package com.example.daewoo.common.config;

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
    private static final String AUTHORIZATION_BASE_URI = "/oauth2/authorization/"; // 기본 경로

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, AUTHORIZATION_BASE_URI.substring(0, AUTHORIZATION_BASE_URI.length() - 1));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {

        // 1. 요청 URL에서 registrationId를 직접 추출 (URL 파싱)
        String requestUri = request.getRequestURI();
        String registrationId = null;

        if (requestUri.startsWith(AUTHORIZATION_BASE_URI)) {
            // /oauth2/authorization/kakao 에서 'kakao' 부분을 추출
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

        // 2. 요청 객체가 존재하고, 해당 요청이 카카오 로그인인지 확인
        if (authorizationRequest != null && KAKAO_REGISTRATION_ID.equals(clientRegistrationId)) {

            // 3. AuthorizationRequest 객체 재구성
            Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

            // 🚨 [필수] prompt=login 파라미터 추가
            additionalParameters.put("prompt", "login");

            // 4. 새로운 요청 객체를 Builder로 생성하여 반환
            authorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(additionalParameters)
                    .build();
        }

        return authorizationRequest;
    }
}