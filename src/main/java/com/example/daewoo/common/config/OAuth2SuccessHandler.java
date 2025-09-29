package com.example.daewoo.common.config;

import com.example.daewoo.common.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets; // 🚨 [필수] 임포트

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String FRONTEND_BASE_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // src/main/java/com/example/daewoo/common/config/OAuth2SuccessHandler.java (수정할 파일)

// ...

// ROLE_PENDING_REGISTRATION 조건문 내부
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PENDING_REGISTRATION"))) {

            // 🚨 [수정된 부분]: 속성 추출 시 null 체크 및 안전한 값 할당 로직 추가
            Object oauthIdObj = oAuth2User.getAttributes().get("oauthId");
            String oauthId = oauthIdObj != null ? String.valueOf(oauthIdObj) : "";

            String registrationId = (String) oAuth2User.getAttributes().get("registrationId");
            if (registrationId == null) registrationId = ""; // null일 경우 빈 문자열

            String email = (String) oAuth2User.getAttributes().get("userEmail");
            if (email == null) email = ""; // null일 경우 빈 문자열

            String nickname = (String) oAuth2User.getAttributes().get("nickname");
            if (nickname == null) nickname = ""; // null일 경우 빈 문자열

            // 이메일이나 닉네임이 빈 값이라도 일단 리다이렉트 URL을 안전하게 생성
            String redirectUrl = UriComponentsBuilder.fromUriString(FRONTEND_BASE_URL)
                    .path("/signup/additional-info")
                    .queryParam("oauthId", oauthId)
                    .queryParam("registrationId", registrationId)
                    .queryParam("email", email)
                    .queryParam("nickname", nickname)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUriString();

// ...

            log.info("Redirecting GUEST user to: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } else {
            // ROLE_USER 등 이미 최종 등록된 사용자: JWT 토큰 발급 및 최종 리다이렉트
            String token = jwtTokenProvider.generateToken(authentication);

            String finalRedirectUrl = UriComponentsBuilder.fromUriString(FRONTEND_BASE_URL)
                    .path("/accommodation")
                    .queryParam("token", token)
                    .build()
                    .toUriString();

            log.info("Redirecting logged-in user to: {}", finalRedirectUrl);
            response.sendRedirect(finalRedirectUrl);
        }
    }
}