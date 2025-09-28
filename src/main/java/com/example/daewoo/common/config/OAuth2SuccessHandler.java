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

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PENDING_REGISTRATION"))) {

            // 정보 추출
            String oauthId = String.valueOf(oAuth2User.getAttributes().get("oauthId"));
            String registrationId = (String) oAuth2User.getAttributes().get("registrationId");
            String email = (String) oAuth2User.getAttributes().get("userEmail");
            String nickname = (String) oAuth2User.getAttributes().get("nickname");

            // ⭐ [최종 수정] build() 호출 전에 .encode()를 명시적으로 호출합니다.
            String redirectUrl = UriComponentsBuilder.fromUriString(FRONTEND_BASE_URL)
                    .path("/signup/additional-info")
                    .queryParam("oauthId", oauthId)
                    .queryParam("registrationId", registrationId)
                    .queryParam("email", email)
                    .queryParam("nickname", nickname)
                    // 🚨 [핵심 수정] build() 전에 UTF-8로 인코딩을 명시적으로 수행
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUriString();

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