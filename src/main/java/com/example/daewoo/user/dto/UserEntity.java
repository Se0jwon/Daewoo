package com.example.daewoo.user.dto;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user_tbl`")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username") // `user_tbl`의 컬럼명과 일치
    private String username;

    @Column(name = "password") // `user_tbl`의 컬럼명과 일치
    private String password;

    @Column(name = "email") // `user_tbl`의 컬럼명과 일치
    private String userEmail;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_birth")
    private LocalDate userBirth;

}