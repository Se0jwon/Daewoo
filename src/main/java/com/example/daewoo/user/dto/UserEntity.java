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

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_pwd")
    private String password;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_birth")
    private LocalDate userBirth;

    @Column(name = "user_email")
    private String userEmail;
}