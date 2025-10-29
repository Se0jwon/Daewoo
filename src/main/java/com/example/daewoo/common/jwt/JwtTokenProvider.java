// JwtTokenProvider.java (수정 완료)
package com.example.daewoo.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User; // ⬅️‼️ OAuth2User 임포트 추가 ‼️
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // ⬇️ ‼️ 수정된 부분 시작 ‼️ ⬇️
        String subject;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // 일반 로그인 시: UserDetails에서 username (이메일) 가져오기
            subject = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            // 소셜 로그인 시: OAuth2User 속성에서 'userEmail' 가져오기
            // (CustomOAuth2UserService에서 'userEmail' 속성을 추가했었음)
            subject = (String) ((OAuth2User) principal).getAttributes().get("userEmail");
            if (subject == null) {
                // 혹시 모를 예외 처리: userEmail 속성이 없다면 getName() (OAuth ID) 사용
                log.warn("OAuth2User attributes does not contain 'userEmail', falling back to getName()");
                subject = authentication.getName(); // 원래 로직대로 OAuth ID 사용
            }
        } else {
            // 기타 경우: 기본 getName() 사용 (예상치 못한 경우)
            log.warn("Unknown principal type: {}, falling back to getName()", principal.getClass().getName());
            subject = authentication.getName();
        }
        // ⬆️ ‼️ 수정된 부분 끝 ‼️ ⬆️


        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 10800000); // 3시간 (10800000ms)

        String accessToken = Jwts.builder()
                // .subject(authentication.getName()) // ⬅️ 기존 코드 주석 처리
                .subject(subject) // ⬅️ 수정된 코드: 이메일 또는 원래 ID를 Subject로 사용
                .claim("auth", authorities)
                .expiration(accessTokenExpiresIn)
                .signWith(key)
                .compact();

        log.info("Generated token with subject: {}", subject); // Subject 값 로그 추가
        return accessToken;
    }

    /**
     * 특정 사용자 이메일을 기반으로 JWT 토큰을 생성합니다. (Social Signup Complete 시 사용)
     * ⭐ ApiUserController.java에서 사용됩니다.
     */
    public String generateTokenFromUserEmail(String email) {
        // 이 메서드는 이미 email을 받으므로 수정할 필요 없음
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails principal = new User(email, "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        return generateToken(authentication); // 수정된 generateToken 메서드를 호출하게 됨
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 여기서는 Subject(이메일)를 username으로 사용하는 UserDetails 객체를 생성합니다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        // SecurityContextHolder에 저장될 Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었더라도 클레임 정보는 필요할 수 있으므로 반환
            return e.getClaims();
        }
    }
}