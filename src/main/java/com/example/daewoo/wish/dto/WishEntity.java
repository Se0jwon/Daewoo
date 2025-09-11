package com.example.daewoo.wish.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wish")
@Builder
public class WishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long wishId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "acc_id") // accommodationId를 acc_id로 변경
    private Long accommodationId;
}