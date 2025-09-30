package com.example.daewoo.user.dto;

import com.example.daewoo.reservation.dto.ReservationEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter // 💡 setRegistrationId, setOauthId, setRole 등이 자동으로 생성됩니다.
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tbl")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String userEmail;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_birth")
    private LocalDate userBirth;

    @Column(name = "image_url")
    private String imageUrl;

    // 💡 [추가] 소셜 서비스 제공자 이름 (kakao, naver, google)
    @Column(name = "registration_id")
    private String registrationId;

    // 💡 [추가] 소셜 서비스의 고유 식별자
    @Column(name = "oauth_id")
    private String oauthId;

    // 💡 [추가] 사용자 권한 (ROLE_USER, ROLE_PENDING_REGISTRATION 등)
    @Column(name = "role")
    private String role;

    @Builder.Default
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // JSON 출력
    private List<ReservationEntity> reservations = new ArrayList<>();
}