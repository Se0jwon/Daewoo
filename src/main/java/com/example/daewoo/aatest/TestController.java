package com.example.daewoo.aatest;

import com.example.daewoo.common.jwt.JwtTokenProvider;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/test/create-and-get-token")
    @Transactional
    public String createAndGetToken(@RequestParam String email) {

        // 1. 이미 해당 이메일로 유저가 있는지 확인
        Optional<UserEntity> existingUser = userRepository.findByUserEmail(email);

        UserEntity userEntity;
        if (existingUser.isPresent()) {
            userEntity = existingUser.get();
        } else {
            // 2. 없다면, 이메일 인증 절차 없이 강제로 유저 생성
            userEntity = UserEntity.builder()
                    .userEmail(email)
                    .username("123")
                    .password(passwordEncoder.encode("123")) // 임시 비밀번호
                    .role("ROLE_USER") // ✨ 인증 완료된 유저로 바로 생성
                    .userBirth(LocalDate.parse("2000-01-01"))
                    .userAddress("123")
                    .userPhone("010-1111-1111")
                    .oauthId(null)
                    // ... UserEntity의 다른 not-null 필드가 있다면 임시값으로 채워주세요 ...
                    .build();
            userRepository.save(userEntity);
        }

        // 3. 생성된 유저 정보로 UserDetails 및 Authentication 객체 생성
        UserDetails userDetails = new User(userEntity.getUserEmail(),
                userEntity.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());

        // 4. JWT 토큰을 발급하여 바로 반환
        return jwtTokenProvider.generateToken(authentication);
    }
}